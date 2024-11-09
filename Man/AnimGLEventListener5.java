package Man;

import Texture.TextureReader;
import com.sun.opengl.util.Animator;
import java.awt.event.*;
import java.io.IOException;
import javax.media.opengl.*;
import java.util.BitSet;
import javax.media.opengl.glu.GLU;

public class AnimGLEventListener5 extends AnimListener {
    Animator animator;
    int animationIndex = 0;
    int monsterIndex = 4;
    int maxWidth = 100;
    int maxHeight = 100;
    int r0sasx = 0;
    int r0sasY = 0;
    int x = maxWidth / 2, y = 1;
    int x1 = maxWidth / 3, y1 = maxHeight - 10;
    double angle = 0;
    int i = 9;
    boolean fire = false; // علشان الرصاصه

    String assetsFolderName = "Assets/Alphabet";
    String textureNames[] = {
            "Man1.png", "Man2.png", "Man3.png", "Man4.png",
            "o.png", "n.png", "p.png", "r.png", "ninja star.png",
            "Health.png", "health1.png", "health2.png", "health3.png", "HealthB.png", "Back.png"
    };
    TextureReader.Texture texture[] = new TextureReader.Texture[textureNames.length];
    int textures[] = new int[textureNames.length];

    public void init(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f); // Clear the background to white
        gl.glEnable(GL.GL_TEXTURE_2D); // Enable texture mapping
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glGenTextures(textureNames.length, textures, 0);

        for (int i = 0; i < textureNames.length; i++) {
            try {
                texture[i] = TextureReader.readTexture(assetsFolderName + "//" + textureNames[i], true);
                gl.glBindTexture(GL.GL_TEXTURE_2D, textures[i]);
                new GLU().gluBuild2DMipmaps(
                        GL.GL_TEXTURE_2D,
                        GL.GL_RGBA,
                        texture[i].getWidth(),
                        texture[i].getHeight(),
                        GL.GL_RGBA,
                        GL.GL_UNSIGNED_BYTE,
                        texture[i].getPixels()
                );
            } catch (IOException e) {
                System.out.println("Error loading texture: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void display(GLAutoDrawable gld) {
        y1--;
        GL gl = gld.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT); // Clear the screen and depth buffer
        gl.glLoadIdentity();

        DrawBackground(gl);
        handleKeyPress();
        animationIndex = animationIndex % 4;

        DrawSprite(gl, x, y, animationIndex, 1);
//       بيشوف العسكري خسر ولا لا
        if (i < 13) {
            DrawSprite(gl, x1, y1, monsterIndex, 1);
        }

        // الرصاص
        if (fire) {
            DrawSprite(gl, r0sasx, r0sasY, 8, 0.5f);
            angle += 20; // دوران النينجا
            if (angle >= 360) {
                angle = 0;
            }

            if (checkCollision(r0sasx, r0sasY, x1, y1)) {
                fire = false; // يقفل الرصاصه
                x1 = (int) (Math.random() * maxWidth);
                y1 = maxHeight;
                monsterIndex = (int) (Math.random() * 4) + 4; // يرجع الوحش
                System.out.println("Goal!");
            }

            if (r0sasY > maxHeight) {
                fire = false; // نخلي رصاص جديد يضرب
            }

            System.out.println("Bullet Position: (" + r0sasx + ", " + r0sasY + "), Angle: " + angle);
        }

        // تحريك الوحش للأسفل
        if (y1 < 0) {
            x1 = (int) (Math.random() * maxWidth);
            y1 = maxHeight;
            if (i == 13) {
                animator.stop();
            }
            i++;
            monsterIndex = (int) (Math.random() * 4) + 4;
            fire = false;
        }

        if (fire) {
            DrawSprite(gl, r0sasx, r0sasY, 8, 1f);
            updateStarPosition();
        }
    }

    double starSpeed = 3.0;
    public void updateStarPosition() {
        double dx = x1 - r0sasx;
        double dy = y1 - r0sasY;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance > 1) {
            r0sasx += (dx / distance) * starSpeed;
            r0sasY += (dy / distance) * starSpeed;
        } else {
            fire = false;
            y1 = maxHeight;
            x1 = (int) (Math.random() * maxWidth);

        }
    }

    public boolean checkCollision(int rosasaX, int rosasaY, int monsterX, int monsterY) {
        int rosasaSize = 5; //الرصاصه
        int monsterSize = 10; // الوحش

        return (rosasaX < monsterX + monsterSize && rosasaX + rosasaSize > monsterX &&
                rosasaY < monsterY + monsterSize && rosasaY + rosasaSize > monsterY);
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    public void DrawSprite(GL gl, int x, int y, int index, float scale) {
        if (index >= 0 && index < textures.length) {
            gl.glEnable(GL.GL_BLEND);
            gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);

            gl.glPushMatrix();
            gl.glTranslated(x / (maxWidth / 2.0) - 0.9, y / (maxHeight / 2.0) - 0.9, 0);

            if (index == 8) {
                gl.glRotated(angle, 0.0, 0.0, 1.0); // Rotate star around Z axis
            }

            gl.glScaled(0.1 * scale, 0.1 * scale, 1);
            gl.glBegin(GL.GL_QUADS);
            gl.glTexCoord2f(0.0f, 0.0f);
            gl.glVertex3f(-1.0f, -1.0f, -1.0f);
            gl.glTexCoord2f(1.0f, 0.0f);
            gl.glVertex3f(1.0f, -1.0f, -1.0f);
            gl.glTexCoord2f(1.0f, 1.0f);
            gl.glVertex3f(1.0f, 1.0f, -1.0f);
            gl.glTexCoord2f(0.0f, 1.0f);
            gl.glVertex3f(-1.0f, 1.0f, -1.0f);
            gl.glEnd();
            gl.glPopMatrix();
            gl.glDisable(GL.GL_BLEND);
        } else {
            System.out.println("Invalid texture index: " + index);
        }
    }

    public void DrawBackground(GL gl) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[textures.length - 1]);

        gl.glPushMatrix();
        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();
        gl.glDisable(GL.GL_BLEND);

        // بنرسم شريط الحياه
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[i]);
        gl.glPushMatrix();
        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, 0.9f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(0.1f, 0.9f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(0.1f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();
        gl.glDisable(GL.GL_BLEND);
    }

    public void handleKeyPress() {
        if (!fire) {
            if (isKeyPressed(KeyEvent.VK_O) && monsterIndex == 4) {
                r0sasx = x;
                r0sasY = y ;
                fire = true;
            } else if (isKeyPressed(KeyEvent.VK_N) && monsterIndex == 5) {
                r0sasx = x;
                r0sasY = y ;
                fire = true;
            } else if (isKeyPressed(KeyEvent.VK_P) && monsterIndex == 6) {
                r0sasx = x;
                r0sasY = y ;
                fire = true;
            } else if (isKeyPressed(KeyEvent.VK_R) && monsterIndex == 7) {
                r0sasx = x;
                r0sasY = y ;
                fire = true;
            }
        }

        // تحقق من حدود العرض للجانب الأيسر
        if (isKeyPressed(KeyEvent.VK_LEFT) && x > 0) {
            x--;
            animationIndex++;
        }

        // تحقق من حدود العرض للجانب الأيمن
        if (isKeyPressed(KeyEvent.VK_RIGHT) && x < maxWidth - 10) {
            x++;
            animationIndex++;
        }
    }


    public BitSet keyBits = new BitSet(256);

    @Override
    public void keyPressed(final KeyEvent event) {
        int keyCode = event.getKeyCode();
        keyBits.set(keyCode);
    }

    @Override
    public void keyReleased(final KeyEvent event) {
        int keyCode = event.getKeyCode();
        keyBits.clear(keyCode);
    }

    @Override
    public void keyTyped(final KeyEvent event) {
    }

    public boolean isKeyPressed(final int keyCode) {
        return keyBits.get(keyCode);
    }
}
