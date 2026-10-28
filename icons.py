# Gera os icones do Android a partir de icon.png e icon-fg.png (corre no GitHub Actions)
from PIL import Image, ImageDraw
import os

res = 'android/app/src/main/res'
ic = Image.open('icon.png').convert('RGBA')
fg = Image.open('icon-fg.png').convert('RGBA')

def mask(size, circle):
    m = Image.new('L', (size * 4, size * 4), 0)
    d = ImageDraw.Draw(m)
    if circle:
        d.ellipse((0, 0, size * 4 - 1, size * 4 - 1), fill=255)
    else:
        d.rounded_rectangle((0, 0, size * 4 - 1, size * 4 - 1), radius=int(size * 4 * 0.22), fill=255)
    return m.resize((size, size), Image.LANCZOS)

for dens, s in {'mdpi': 48, 'hdpi': 72, 'xhdpi': 96, 'xxhdpi': 144, 'xxxhdpi': 192}.items():
    folder = os.path.join(res, 'mipmap-' + dens)
    os.makedirs(folder, exist_ok=True)
    base = ic.resize((s, s), Image.LANCZOS)
    for name, circle in (('ic_launcher.png', False), ('ic_launcher_round.png', True)):
        out = Image.new('RGBA', (s, s), (0, 0, 0, 0))
        out.paste(base, (0, 0), mask(s, circle))
        out.save(os.path.join(folder, name))
    f = int(s * 108 / 48)
    fg.resize((f, f), Image.LANCZOS).save(os.path.join(folder, 'ic_launcher_foreground.png'))

with open(os.path.join(res, 'values', 'ic_launcher_background.xml'), 'w') as fh:
    fh.write('<?xml version="1.0" encoding="utf-8"?>\n<resources>\n    <color name="ic_launcher_background">#060912</color>\n</resources>\n')
print('icones gerados')
