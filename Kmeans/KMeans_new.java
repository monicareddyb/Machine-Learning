
/*** Author :Vibhav Gogate
The University of Texas at Dallas
*****/


import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Random;

 

public class KMeans_new {
    public static void main(String [] args){
	if (args.length < 3){
	    System.out.println("Usage: Kmeans <input-image> <k> <output-image>");
	    return;
	}
  Long a = new Long(5);
  a.byteValue();
	try{
	    BufferedImage originalImage = ImageIO.read(new File(args[0]));
	    int k=Integer.parseInt(args[1]);
	    BufferedImage kmeansJpg = kmeans_helper(originalImage,k);
	    ImageIO.write(kmeansJpg, "jpg", new File(args[2])); 
	    
	}catch(IOException e){
	    System.out.println(e.getMessage());
	}	
    }
    
    private static BufferedImage kmeans_helper(BufferedImage originalImage, int k){
	int w=originalImage.getWidth();
	int h=originalImage.getHeight();
	BufferedImage kmeansImage = new BufferedImage(w,h,originalImage.getType());
	Graphics2D g = kmeansImage.createGraphics();
	g.drawImage(originalImage, 0, 0, w,h , null);
	// Read rgb values from the image
	int[] rgb=new int[w*h];
	int count=0;
	for(int i=0;i<w;i++){
	    for(int j=0;j<h;j++){
		rgb[count++]=kmeansImage.getRGB(i,j);
	    }
	}
	// Call kmeans algorithm: update the rgb values
	kmeans(rgb,k);

	// Write the new rgb values to the image
	count=0;
	for(int i=0;i<w;i++){
	    for(int j=0;j<h;j++){
		kmeansImage.setRGB(i,j,rgb[count++]);
	    }
	}
	return kmeansImage;
    }

    // Your k-means code goes here
    // Update the array rgb by assigning each entry in the rgb array to its cluster center
    private static void kmeans(int[] rgb, int k){

    int[] a = new int[k];   
    int[] b = new int[k];   
    int[] pixels = new int[k];  
    int[] red_cluster = new int[k];   
    int[] green_cluster = new int[k]; 
    int[] blue_cluster = new int[k];  
    int[] new_cluster = new int[rgb.length]; 
    int count = 0;        

    double max_dist = Double.MAX_VALUE;   
    double dist = 0;                   
    int nearest_dist = 0;               
    
   
    for ( int i = 0; i < k; i++ ) {
      Random r = new Random();
      int centerValue = 0;
      centerValue = rgb[r.nextInt( rgb.length )];
      b[i] = centerValue;
    }

 int iterator=0;
    do {
      for ( int i = 0; i < b.length; i++ ) {
        a[i] = b[i];
        pixels[i] = 0;
        red_cluster[i] = 0;
        green_cluster[i] = 0;
        blue_cluster[i] = 0;
      }

      for ( int i = 0; i < rgb.length; i++ ) {
        max_dist = Double.MAX_VALUE;

        for ( int j = 0; j < b.length; j++ ) {
          dist = pixel_dist( rgb[i], b[j] );
          if ( dist < max_dist ) {
            max_dist = dist;
            nearest_dist = j;
          }
        }

        new_cluster[i] = nearest_dist;
        pixels[nearest_dist]++;
        red_cluster[nearest_dist] +=   ((rgb[i] & 0x00FF0000) >>> 16);
        green_cluster[nearest_dist] += ((rgb[i] & 0x0000FF00) >>> 8);
        blue_cluster[nearest_dist] +=  ((rgb[i] & 0x000000FF) >>> 0);
      }

      for ( int i = 0; i < b.length; i++ ) {
        int red =   (int)((double)red_cluster[i] /   (double)pixels[i]);
        int green = (int)((double)green_cluster[i] / (double)pixels[i]);
        int blue =  (int)((double)blue_cluster[i] /  (double)pixels[i]);

        b[i] = 
                            ((red & 0x000000FF) << 16) |
                            ((green & 0x000000FF) << 8) |
                            ((blue & 0x000000FF) << 0);
iterator++;
      }
    } while( iterator<30 );

    for ( int i = 0; i < rgb.length; i++ ) {
      rgb[i] = b[new_cluster[i]];
    }
  }

  private static boolean isConverged( int[] a, int[] b ) {
    for ( int i = 0; i < b.length; i++ )
      if ( a[i] != b[i] )
        return false;

    return true;
  }

  private static double pixel_dist( int pixelA, int pixelB ) {
    int differenceOfRed = ((pixelA & 0x00FF0000) >>> 16) - ((pixelB & 0x00FF0000) >>> 16);
    int differenceOfGreen = ((pixelA & 0x0000FF00) >>> 8)  - ((pixelB & 0x0000FF00) >>> 8);
    int differenceOfBlue = ((pixelA & 0x000000FF) >>> 0)  - ((pixelB & 0x000000FF) >>> 0);
    return Math.sqrt( differenceOfRed*differenceOfRed + differenceOfGreen*differenceOfGreen + differenceOfBlue*differenceOfBlue );
    }

}