import base64, os, sys
b64 = os.environ.get('KS_B64', '').strip()
if not b64:
    print('AVISO: KS_B64 não definido; a compilar com chave de teste (o APK não instala por cima do anterior).')
    sys.exit(0)
open('android/app/norte.jks', 'wb').write(base64.b64decode(b64))
p = 'android/app/build.gradle'
s = open(p, encoding='utf-8').read()
assert 'android {' in s, 'build.gradle inesperado'
blk = '''android {
    signingConfigs {
        debug {
            storeFile file("norte.jks")
            storePassword System.getenv("KS_PASS")
            keyAlias System.getenv("KS_ALIAS")
            keyPassword System.getenv("KS_PASS")
        }
    }
'''
s = s.replace('android {\n', blk, 1)
open(p, 'w', encoding='utf-8').write(s)
print('Chave fixa aplicada.')
