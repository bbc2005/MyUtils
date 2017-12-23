package com.lzg.test;/*
      Copyright M-Gate Labs 2007
  */

/**
 <p>
 <b>Command Line</b>
 </p>
 <p>
 Simple Command Line Interface for Flash Exploit
 </p>
 */

import com.mgatelabs.swftools.exploit.conversion.MorphConverter;
import com.mgatelabs.swftools.support.plugins.AdvancedConverter;
import com.mgatelabs.swftools.support.swf.io.FlashFilter;
import com.mgatelabs.swftools.support.swf.io.FlashReader;
import com.mgatelabs.swftools.support.swf.objects.*;
import com.mgatelabs.swftools.support.swf.tags.PlaceObject;
import com.mgatelabs.swftools.support.swf.tags.Tag;

import java.io.File;
import java.util.List;
import java.util.Vector;
/**
 * swf导出图片
 * @ClassName: SwfTest
 * @Description: TODO
 * @author lzg
 * @date 2017年12月16日 下午12:56:16
 */
public class SwfTest {

    public static void main(String args[]) {
    	long start = System.currentTimeMillis();
    	System.out.println("====================================================================");
        System.out.println("开始执行时间：" + start);
        System.out.println("====================================================================");

        File baseDir = new File(".");
        File outDir = new File(baseDir, "out");
        outDir.mkdirs();
        
        //输入SWF文件
//        File inputFile =  new File(baseDir + "/in/电子书.swf");
        File inputFile = null;
        //输出文件目录
        File outputFile = new File(baseDir, "out");
        
        String dir = baseDir.toString() + "/in";
        List<String> filePathList = Swf2ImgUtil.getFileList(dir);
        
        int count = 0;
        for (String path : filePathList) {
			System.out.println("要解析的文件路径：" + path);
			inputFile = new File(path);
			count +=export(inputFile, outputFile);
		}
        
        System.out.println("====================================================================");
        System.out.println(">>>>>处理完成!");
        System.out.println("一共导出：" + count + "张图片");
        long end = System.currentTimeMillis();
        System.out.println("结束执行时间：" + end);
        System.out.println("执行用时：" + (end - start) + "ms");
        System.out.println("====================================================================");
        System.exit(0);
    }
    
    public static int export(File inputFile, File outputFile){

        // Test Stuff First
        if (inputFile == null || outputFile == null) {
            return 0;
        }
        if (!inputFile.exists()) {
            System.out.println("输入文件不存在！");
            return 0;
        } 
        
        long start = System.currentTimeMillis();
        System.out.println(inputFile.getName() + "处理开始执行时间：" + start);
        
        boolean parseShapes = true;
        boolean parseFonts = false;
        boolean parseBitmaps = true;
        boolean parseSounds = false;

        boolean specialMoveShape = true;
        
        
        if (outputFile.exists() && outputFile.isDirectory()) {

        } else if ((outputFile.exists() && !outputFile.isDirectory()) || !outputFile.exists()) {
            // Make the Directories
            outputFile.mkdirs();
        }

        // Make A Flash Filter
        // Used to Skip Tags, Like Sounds, Fonts...
        FlashFilter aFilter = new FlashFilter();

        // Only Parse Shape
        aFilter.setFilter(FlashFilter.SHAPE, parseShapes);
        aFilter.setFilter(FlashFilter.SOUND, parseSounds);
        aFilter.setFilter(FlashFilter.FONT, parseFonts);
        aFilter.setFilter(FlashFilter.BITMAP, parseBitmaps);
        aFilter.setFilter(FlashFilter.BITMAP_DISCARD, !parseBitmaps);


        System.out.println("过滤信息：");
        System.out.println("\tShapes\t= " + aFilter.getFilter(FlashFilter.SHAPE));
        System.out.println("\tFonts\t= " + aFilter.getFilter(FlashFilter.FONT));
        System.out.println("\tImages\t= " + aFilter.getFilter(FlashFilter.BITMAP));
        System.out.println("\tSound\t= " + aFilter.getFilter(FlashFilter.SOUND));
        System.out.println("");

        // Make a Flash Reader, Which parses the Flash File
        FlashReader myReader = new FlashReader(inputFile, aFilter);

        // Set the Result to Null
        FMovie myFlashObject = null;

        // Test is the Reader Actually Works
        if (myReader.ready()) {
            // Parse Flash File and Return a Movie Object
            try {
                myFlashObject = myReader.parse();
            } catch (Exception e) {
                System.out.println(e);
                e.printStackTrace();
                System.exit(1);
            }
        }
        // Close the Reader
        myReader.close();

        // Test if the Parser Failed, Usually Overbyte Errors, i.e. Reading too Far
        if (myFlashObject == null) {
            System.out.println("Error While Reading Flash Format: " + myReader.getError());
            System.exit(1);
        }

        // Clean up
        myReader = null;

        // Advanced Conversion (!Overhead! Ram Intensive!)

        // Convert Shapes
        if (myFlashObject.getObjectVector().size() > 0) {
            AdvancedConverter ac = new AdvancedConverter();
//            System.out.println("Conversion Engine: " + ac.getName() + "\n");
            ac.update(myFlashObject);
            ac = null;
        }

        // Convert Morphs
        if (myFlashObject.getObjectVector().size() > 0 && myFlashObject.ready()) {
            MorphConverter mc = new MorphConverter();
            mc.update(myFlashObject);
            mc = null;
        }


		System.out.println(inputFile.getName() + "开始解析......");

		FMovieData myMovieData = new FMovieData(myFlashObject);
		Vector myObjects = myFlashObject.getObjectVector();

		String inputFileName = inputFile.getName();
		inputFileName = inputFileName.substring(0,inputFileName.lastIndexOf("."));
		
		String path = outputFile.toString();
		if (!path.endsWith("/") && !path.endsWith("\\")) {
			path = path + "/";
		}
		String parentName = inputFile.getParentFile().getName();
		path += parentName;
		File dir = new File(path);
		if(!dir.exists()){
			dir.mkdirs();
		}
        path += "/" +inputFileName;
//        System.out.println("path=" + path);
		Swf2ImgUtil export = new Swf2ImgUtil(path);
		
		System.out.println("Shape个数=" + myObjects.size());
        for (int i = 0; i < myObjects.size(); i++) {
            Object o = myObjects.get(i);

            if (o instanceof FShape) {
                FShape aShape = (FShape) o;
                int sid = aShape.getID();
                System.out.println("正在处理 Shape sid: " + sid);
                FSprite tempSprite = new FSprite(-1, 1, myFlashObject);

                Tag placeTag = new Tag(26, 1); // Place Object
                Tag showTag = new Tag(1, 0); // Show Object
                Tag stopTag = new Tag(0, 0); // Stop Object

                FRect sRect = aShape.getRect();
                FMatrix pMatrix = new FMatrix();
                if (specialMoveShape) {
                    if (sRect.getLeft() < 0) {
                        pMatrix.setTranslateX(sRect.getLeft() * -1);
                    }
                    if (sRect.getTop() < 0) {
                        pMatrix.setTranslateY(sRect.getTop() * -1);
                    }
                }

                PlaceObject p02 = new PlaceObject();
                p02.setPlaceFlagHasCharacter();
                p02.setPlaceFlagHasMatrix();

                p02.setID(aShape.getID());
                p02.setMatrix(pMatrix);
                p02.setDepth(1);

                placeTag.myObject = (p02);

                tempSprite.addTag(placeTag);
                tempSprite.addTag(showTag);
                tempSprite.addTag(stopTag);

                FMovieData newMovie = new FMovieData(tempSprite);
                newMovie.nextFrame();
                //执行导出
                export.createLayer(newMovie.getDisplayList(), null);
            }
        }
    

        System.out.println(inputFile.getName() + "处理完成!");
        System.out.println(inputFile.getName() + "一共导出：" + export.getCount() + "张图片");
        long end = System.currentTimeMillis();
        System.out.println(inputFile.getName() + "处理结束执行时间：" + end);
        System.out.println(inputFile.getName() + "处理执行用时：" + (end - start) + "ms");
        
        return export.getCount();
    }
    
    
}