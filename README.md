# Multi-Texture Bug

Simple app created to demonstrate bug when implementing both mulit-texturing and drawing GL_LINES.

---

## Setup
I am drawing four triangles, each using the same class Model.  They are drawn in the following order:

1. Top-left: Solid color with outline
2. Top-right: Single-texture
3. Bottom-left: Two textures
4. Bottom-right: Outline only

When all four triangles are drawn, the the outlines of both 1 and 4 are faded.  If I remove 3, the outlines of 1 and 4 look normal.

Triangle 2 (with only a single texture) does not affect triangles 1 and 4.

---

## Screenshots
<img src="https://docs.google.com/open?id=0B23IGY-IeqHXMUZCa3Z4N2JHUXM" />

Outlines on triangles 1 and 4 are faded.

<img src="https://docs.google.com/open?id=0B23IGY-IeqHXTllhM0JJSWdOSW8" />

Outlines on triangles 1 and 4 look normal.