package com.blm.qiubopay.tools.Sign;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class BmpFile {
	//width & height of image
	private int width,height;
	private ByteBuffer buffer = null;
	private int n;
	private int r;
	private int nc;
	public short Tt = 128;
	
	//bitmapfileheader
	int bfType = 0x4d42;//file type
	int bfSize;//file size
	int bfReserved1 = 0;//reserved
	int bfReserved2 = 0;//reserved
	int bfOffBits = 62;//data offset

	//bitmapinfoheader
	int biSize = 40;//bitmap info header size
	int biWidth;//width
	int biHeight;//height
	int biPlanes = 1;//planes
	int biBitcount = 1;//bits per pixel
	int biCompression = 0;//whether compressed
	int biSizeImage;//data size
	int biXPelsPerMeter = 0;//hResolution
	int biYPelsPerMeter = 0;//vResolution
	int biClrUsed = 2;//colors
	int biClrImportant = 0;//important colors
	
	public BmpFile(int width, int height) {
		// TODO Auto-generated constructor stub
		this.width = width;
		this.height = height;
		
		r = width%8;
		n = width/8;
		int rb = n;
		if(r > 0)
			rb++;
		nc = 0;
		if((rb%4) > 0)
			nc = 4 - rb%4;
		else if(rb<4)
			nc = 4 -rb;
		int dataSize = (rb+nc)*height;
		
		bfSize = 62 + dataSize;
		buffer = ByteBuffer.allocate(bfSize);
		System.out.println("length---->"+bfSize);
		System.out.println("width-->"+this.width+" height---->"+this.height);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		biWidth = width;
		biHeight = height;
		biSizeImage = dataSize;
		
		
	}
	
	public byte[] getBmpFile(ByteBuffer imageBuffer){
		buffer.putShort((short) bfType);
		buffer.putInt(bfSize);
		buffer.putShort((short) bfReserved1);
		buffer.putShort((short) bfReserved2);
		buffer.putInt(bfOffBits);
		
		buffer.putInt(biSize);
		buffer.putInt(biWidth);
		buffer.putInt(biHeight);
		buffer.putShort((short) biPlanes);
		buffer.putShort((short) biBitcount);
		buffer.putInt(biCompression);
		buffer.putInt(biSizeImage);
		buffer.putInt(biXPelsPerMeter);
		buffer.putInt(biYPelsPerMeter);
		buffer.putInt(biClrUsed);
		buffer.putInt(biClrImportant);
		

		byte[] array = imageBuffer.array();
		buffer.putInt(0);
		buffer.putInt(0x00FFFFFF);
		
		short[] bArray = new short[array.length / 4];
		for(int i=0;i<array.length;i+=4){
			short R,G,B;
			byte t = array[i];
			if(t<0) R = (short) (256 + t); else R = t;
			t = array[i+1];
			if(t<0) G = (short) (256 + t); else G = t;
			t = array[i+2];
			if(t<0) B = (short) (256 + t); else B = t;
			bArray[i/4] = (short) ((299*R + 578*G +114*B) /1000);
		}

		for(int i=height -1;i >= 0; i--){
			byte b = 0;
			int k = 0;
			for(int j=0;j<n;j++){
				b=0;
				if(bArray[i*width + j*8] > Tt)
					b |= 0x80;
				for(k=1;k<8;k++){
					b |= ((byte)(bArray[i*width + j*8 +k]>Tt ? 1:0)) << (7-k);
				}
				buffer.put(b);
			}

			if(r > 0)
			{
				b = 0;

				if(bArray[i*width + n*8 ]>Tt)
					b|= 0x80;
				for(k=1;k<r;k++)
					b |= ((byte)(bArray[i*width + n*8 +k]>Tt ? 1:0)) << (7-k);
				
				buffer.put(b);
			}

			k=0;
			while(k < nc){
				buffer.put((byte) 0);
				k++;
			}
		}
		
		return buffer.array();
	}
}
