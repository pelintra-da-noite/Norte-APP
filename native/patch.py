p = 'android/app/src/main/AndroidManifest.xml'
s = open(p, encoding='utf-8').read()
assert '</application>' in s, 'manifest inesperado'
perm = ('    <uses-permission android:name="android.permission.QUERY_ALL_PACKAGES" />\n'
        '    <uses-permission android:name="android.permission.PACKAGE_USAGE_STATS" />\n')
s = s.replace('</application>', '</application>\n' + perm, 1)
open(p, 'w', encoding='utf-8').write(s)
print(s)
