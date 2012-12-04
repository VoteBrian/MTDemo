# Multi-Texture Bug

Simple app created to demonstrate bug when implementing both mulit-texturing and drawing GL_LINES.

*(Solution added below)*

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
Outlines on triangles 1 and 4 are faded.
<img src="http://i.imgur.com/Id2e7.png" />


Outlines on triangles 1 and 4 look normal.
<img src="http://i.imgur.com/GZwf6.png" />


---

## Solution
To avoid conflicts with future draws, I needed to revert to gl.glActiveTexture(GL10.GL_TEXTURE0) after the Texture 2 block in Model.java.

	// Texture 1
	gl.glClientActiveTexture(GL10.GL_TEXTURE0);
	gl.glActiveTexture(GL10.GL_TEXTURE0);
	gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	gl.glBindTexture(GL10.GL_TEXTURE_2D, mTexture[0]);
	gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_MODULATE);
	gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTexCoordBuffer1);
	gl.glEnable(GL10.GL_TEXTURE_2D);

	// Texture 2
	gl.glClientActiveTexture(GL10.GL_TEXTURE1);
	gl.glActiveTexture(GL10.GL_TEXTURE1);
	gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	gl.glBindTexture(GL10.GL_TEXTURE_2D, mTexture[1]);
	gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_DECAL);
	gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTexCoordBuffer2);
	gl.glEnable(GL10.GL_TEXTURE_2D);

	gl.glActiveTexture(GL10.GL_TEXTURE0);

The outlines are now correctly drawn along with the multi-textured object.
<img src="http://i.imgur.com/0iZ8C.png" />
