package sledzenie.gry.projekt.spaceshipcontroller;

import java.nio.ByteBuffer;

/**
 * Created by domin4815 on 28.05.16.
 */
public class ControllerData {

    public float x;
    public float y;
    public float z;
    public float speed;

    public ControllerData(float x, float y, float z, float speed) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.speed = speed;
    }

    public byte[] toBytes(){

        byte arrays[][] = new byte[4][4];
        arrays[0] = ByteBuffer.allocate(4).putFloat(x).array();
        arrays[1] = ByteBuffer.allocate(4).putFloat(y).array();
        arrays[2] = ByteBuffer.allocate(4).putFloat(z).array();
        arrays[3] = ByteBuffer.allocate(4).putFloat(speed).array();


        byte[] bytes = new byte[16];
        for (int i=0; i< 4; i++){
            for (int j = 0; j<4 ; j++){
                bytes[4*i + j]= arrays[i][j];
            }
        }
        return bytes;
    }
}
