package com.lzg.test;

import com.mgatelabs.swftools.exploit.conversion.MorphShape;
import com.mgatelabs.swftools.exploit.render.AdvancedPath;
import com.mgatelabs.swftools.support.swf.objects.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * swf导出图片工具
 * @ClassName: Swf2ImgUtil
 * @Description: TODO
 * @author lzg
 * @date 2017年12月17日 下午1:04:20
 */
public class Swf2ImgUtil {
	
	private int count = 0;
	private String path;
	
	// Create layers based on the render data
    public void createLayer(Vector renderData, FDisplayListItem startItem){

        for (int index = 0; index < renderData.size(); index++) {
            FDisplayListItem myItem = (FDisplayListItem) renderData.get(index);

            if (myItem == null) {
                continue;
            }
            
            if (!myItem.getClipLayer() && myItem.isVisible()) {

            	  if (myItem.getObject() instanceof FShape) {
                      FShape fs = (FShape) myItem.getObject();
                      createShape(fs, null, myItem);
                  } else if (myItem.getObject() instanceof FMorph) {
                      FMorph fm = (FMorph) myItem.getObject();
                      createShape(null, MorphShape.getAdvancedPathVector(fm, myItem.getRatio() / 65535.0f), myItem);
                  } else if (myItem.getObject() instanceof FMovieData) {
                      FMovieData fmd = (FMovieData) myItem.getObject();
                      createLayer(fmd.getDisplayList(), myItem);
                  } 
            }
            
        }

    }

    

    /**
     * 导出图片
     * @Title: createShape
     * @Description: TODO
     * @param @param fs
     * @param @param data
     * @param @param myItem
     * @return void
     * @throws
     */
    public void createShape(FShape fs, Vector data, FDisplayListItem myItem) {
//    	System.out.println("=========================createShape=========================");

        if (data == null) {
            data = fs.getRenderData();
        }
//        System.out.println("data.size()=" + data.size());
        for (int index = 0; index < data.size(); index++) {
        	
            AdvancedPath aPath = (AdvancedPath) data.get(index);

            if (aPath.getFill() != null || aPath.getLine() != null || (aPath.getFill() == null && aPath.getLine() == null)) {

                FFill basicFillStyle = aPath.getFill();

//                System.out.println("basicFillStyle.getStyle()=" + (basicFillStyle != null?basicFillStyle.getStyle():"null"));
                if (basicFillStyle != null && (basicFillStyle.getStyle() == FFill.TBITMAP || basicFillStyle.getStyle() == FFill.CBITMAP)) {

                    FTexture ftex = (FTexture) basicFillStyle;
                    FImage fbit = ftex.getBitmap();

                    //System.out.println("Exporting Bitmap: " + ((FBitmap)fbit).getData());

                    if (fbit == null) {
                        //System.out.println("Could Not Locate Bitmap");
                        continue;
                    }

                    String bitmapFullName = null;
                    if (fbit instanceof FBitmap && ((FBitmap) fbit).getVersion() < 3) {
//                        System.out.println("导出图片格式: JPG");
                    	count++;
                    	bitmapFullName = path + "_" + count + ".jpg";
//                    	bitmapFullName = path + ".jpg";
                    } else {
//                    	System.out.println("导出图片格式: PNG");
                    	count++;
                    	bitmapFullName = path + "_" + count + ".png";
                    }

                    if ((((fbit instanceof FBitmap) && ((FBitmap) fbit).getData() != null) || ((FBitmap) fbit).getVersion() == 3) || fbit instanceof FLossless) {

                        try {
                            if (fbit instanceof FBitmap && ((FBitmap) fbit).getVersion() < 3) {
                                if (((FBitmap) fbit).getData() != null) {
                                    FileOutputStream fos = new FileOutputStream(new File(bitmapFullName));
                                    byte[] bitData = ((FBitmap) fbit).getData();

                                    for (int x = 0; x < bitData.length; x++) {
                                        fos.write(bitData[x]);
                                    }

                                    fos.close();
                                }
                            } else {
                                javax.imageio.ImageIO.write(fbit.getImage(), "PNG", new File(bitmapFullName));
                            }
                        } catch (Exception e) {
                        	e.printStackTrace();
                        }
                    } 
                
                }
            }
        }
    }

    /**
     * 获得目录下所有swf文件路径
     * @Title: getFileList
     * @Description: TODO
     * @param @param strPath
     * @param @return
     * @return List<File>
     * @throws
     */
    public static List<String> getFileList(String strPath) {
    	List<String> filePathList = new ArrayList<String>();
        File dir = new File(strPath);
        if( !dir.isDirectory()){
        	System.out.println(dir.getName() + "不是一个目录！");
        	return null;
        }
        File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                
                if (files[i].isDirectory()) { // 判断是文件还是文件夹
//                    fileList.addAll(getFileList(files[i].getAbsolutePath())); // 获取文件绝对路径
                	filePathList.addAll(getFileList(files[i].getPath()));
                } else if (files[i].isFile() && fileName.endsWith(".swf")) { // 判断文件名是否以.swf结尾
                    String strFileName = files[i].getAbsolutePath();
//                    System.out.println("---" + files[i].getPath());
                    filePathList.add(files[i].getPath());
                }
            }

        }
        return filePathList;
    }


	public Swf2ImgUtil(String path) {
		super();
		this.path = path;
	}
	public Swf2ImgUtil() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}