import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class ImageMerge {

    private static ArrayList<Integer> counts = new ArrayList();

    public static void main(String[] args) {


        File bgDir = new File("D:\\1tianhe\\文件\\莱布\\莱布合成\\背景");
        File bodyDir = new File("D:\\1tianhe\\文件\\莱布\\莱布合成\\身体");

        new Thread(){
            @Override
            public void run() {
                super.run();
                mergeStart(3, bgDir, bodyDir);
            }
        }.start();

        new Thread(){
            @Override
            public void run() {
                super.run();
                mergeStart(2, bgDir, bodyDir);
            }
        }.start();


//        mergeStart(1, bgDir, bodyDir);

    }

    private static void mergeStart(int level, File bgDir, File bodyDir){
        File srcDir = new File("D:\\1tianhe\\文件\\莱布\\莱布合成\\SSR");
        File outDir = null;
        switch (level){
            case 1:
                srcDir = new File("D:\\1tianhe\\文件\\莱布\\莱布合成\\R");
                outDir = new File("D:\\1tianhe\\文件\\莱布\\莱布合成\\合成输出\\R");
                break;
            case 2:
                srcDir = new File("D:\\1tianhe\\文件\\莱布\\莱布合成\\SSR");
                outDir = new File("D:\\1tianhe\\文件\\莱布\\莱布合成\\合成输出\\SR");
                break;
            case 3:
                srcDir = new File("D:\\1tianhe\\文件\\莱布\\莱布合成\\SSR");
                outDir = new File("D:\\1tianhe\\文件\\莱布\\莱布合成\\合成输出\\SSR");
                break;
        }

        deleteDirectory(outDir.getAbsolutePath());
        outDir.mkdir();
        //遍历角色目录
        for(File subdir : srcDir.listFiles()){
            if(subdir.isDirectory()){
                String subDirName = subdir.getName();
                System.out.println(subDirName+"============================================");
                sortFiles(level, subDirName, bgDir, bodyDir,subdir, outDir);
            }
        }
        int allCount = 0;
        for(int count : counts){
            allCount += count;
        }
        System.out.println("★★★★★★"+level+ "---> 全部合成完毕，总共"+allCount+"个结果");
    }

    /**
     * 遍历一个子目录中的元素进行合成
     * @param level  等级 1、R  2、SR  3、SSR
     * @param subname
     * @param bgDir
     * @param bodyDir
     * @param srcDir
     * @param outDir
     */
    private static void sortFiles(int level, String subname, File bgDir, File bodyDir, File srcDir, File outDir){
        ArrayList<File> bgs = new ArrayList();   //背景
        ArrayList<File> bodys = new ArrayList();   //身体
        ArrayList<File> trousers = new ArrayList(); //裤子
        ArrayList<File> clothes = new ArrayList();  //衣服
        ArrayList<File> hats = new ArrayList();    //帽子
        ArrayList<File> holds = new ArrayList();   //手持
        if(!srcDir.isDirectory()){
            System.out.println("xxx错误目录");
        }else{
//            for(File file : bgDir.listFiles()){
//                bgs.add(file);
//            }
            if(level!=1) {
                for (File file : bodyDir.listFiles()) {
                    bodys.add(file);
                }
            }
            for(File file : srcDir.listFiles()){
                if(file.isDirectory())
                    continue;
                if(file.getName().contains("背景")){
                    bgs.add(file);
                }else
               if(level==1 && file.getName().contains("身体")){
                    bodys.add(file);
                }else
                if(file.getName().contains("裤子")){
                    trousers.add(file);
                }else if(file.getName().contains("衣服")){
                    clothes.add(file);
                }else if(file.getName().contains("手持")||
                        file.getName().contains("火")||
                        file.getName().contains("旗子")||
                        file.getName().contains("武器")){
                    holds.add(file);
                }else if(file.getName().contains("皇冠")||
                        file.getName().contains("帽子")||
                        file.getName().contains("头角")||
                        file.getName().contains("眼镜")||
                        file.getName().contains("头盔")){
                    hats.add(file);
                }
            }
            System.out.println("背景 -- "+bgs.size());
            System.out.println("身体 -- "+bodys.size());
            System.out.println("裤子 -- "+trousers.size());
            System.out.println("衣服 -- "+clothes.size());
            System.out.println("手持 -- "+holds.size());
            System.out.println("帽子 -- "+hats.size());

            String type = "png";
            int number = 0;

            ArrayList<File> before;
            ArrayList<File> after;
            if(subname.contains("厨师") || subname.contains("画家") ||subname.contains("魔术师")){
                //长衣服，先画裤子
                before = trousers;
                after = clothes;
            }else{
                before = clothes;
                after = trousers;
            }
            switch (level){
                case 1:   //R 背景、身体、衣服、裤子、(没有帽子)、(没有手持)
                    for(File bg : bgs){
                        for(File body : bodys){
                            for(File be : before){
                                for(File af : after){
                                    String outName = subname+"-R-"+(++number)+"."+type;
                                    System.out.println("-- "+outName+" -- "+bg.getName()+"  "+body.getName()+"  "+be.getName()+"  "+af.getName());
                                    if(be.getName().contains("厨师") || be.getName().contains("画家") ||be.getName().contains("魔术师")){
                                        System.out.println("--三种长衣服，先画裤子  "+be.getName());
                                        merge(
                                                new File[]{bg, body, af, be},
                                                type,
                                                new File(outDir, outName)
                                        );
                                    }else{
                                        merge(
                                                new File[]{bg, body, be, af},
                                                type,
                                                new File(outDir, outName)
                                        );
                                    }
                                }
                            }
                        }
                    }
                    break;
                case 2:    //SR 背景、身体、衣服、裤子、(没有帽子)、手持
                    for(File bg : bgs){
                        for(File body : bodys){
                            for(File be : before){
                                for(File af : after){
                                    for(File hold : holds){
                                        String outName = subname+"-SR-"+(++number)+"."+type;
                                        System.out.println("-- "+outName+" -- "+bg.getName()+"  "+body.getName()+"  "+be.getName()+"  "+af.getName()+"  "+hold.getName());
                                        merge(
                                                new File[]{bg, body, be, af, hold},
                                                type,
                                                new File(outDir, outName)
                                        );
                                    }
                                }
                            }
                        }
                    }
                    break;
                case 3:    //SSR 背景、身体、衣服、裤子、帽子、手持
                    for(File bg : bgs){
                        for(File body : bodys){
                            for(File be : before){
                                for(File af : after){
                                    for(File hat: hats){
                                        for(File hold : holds){
                                            String outName = subname+"-SSR-"+(++number)+"."+type;
                                            System.out.println("-- "+outName+" -- "+bg.getName()+"  "+body.getName()+"  "+be.getName()+"  "+af.getName()+"  "+hat.getName()+"  "+hold.getName());
                                            merge(
                                                    new File[]{bg, body, be, af, hat, hold},
                                                    type,
                                                    new File(outDir, outName)
                                            );
                                        }
                                    }
                                }
                            }
                        }
                    }
                    break;
            }

            counts.add(number);
        }
    }


    /**
     * 合并多张图为一张
     * @param imgs
     * @param type
     * @param outFile
     * @return
     */
    private static boolean merge(File[] imgs, String type ,File outFile) {
        int dstHeight = 0;
        int dstWidth = 0;
        int len = imgs.length;
        BufferedImage[] images = new BufferedImage[len];
        int[][] imageArrays = new int[len][];
        for (int i = 0; i < len; i++) {
            try {
                images[i] = ImageIO.read(imgs[i]);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

            int width = images[i].getWidth();
            int height = images[i].getHeight();

            // 从图片中读取RGB 像素
            imageArrays[i] = new int[width * height];
            imageArrays[i] = images[i].getRGB(0, 0, width, height, imageArrays[i], 0, width);

            // 计算合并的宽度和高度
            dstWidth = dstWidth > width ? dstWidth : width;
            dstHeight = dstHeight > height ? dstHeight : height;
        }
        // 生成新图片
        try {
            BufferedImage imageNew = new BufferedImage(dstWidth, dstHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = imageNew.createGraphics();
            for (int i = 0; i < images.length; i++) {
                graphics.drawImage(images[i], 0,0,images[i].getWidth(),images[i].getHeight(), null);
//                System.out.println("绘制图层"+i+"  "+imgs[i].getAbsolutePath());
            }
            graphics.dispose();
            // 写图片，输出到硬盘
            ImageIO.write(imageNew, type, outFile);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * 删除文件夹
     * 删除文件夹需要把包含的文件及文件夹先删除，才能成功
     *
     * @param directory 文件夹名
     * @return 删除成功返回true,失败返回false
     */
    public static boolean deleteDirectory(String directory) {
        // directory不以文件分隔符（/或\）结尾时，自动添加文件分隔符，不同系统下File.separator方法会自动添加相应的分隔符
        if (!directory.endsWith(File.separator)) {
            directory = directory + File.separator;
        }
        File directoryFile = new File(directory);
        // 判断directory对应的文件是否存在，或者是否是一个文件夹
        if (!directoryFile.exists() || !directoryFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        // 删除文件夹下的所有文件和文件夹
        File[] files = directoryFile.listFiles();
        for (int i = 0; i < files.length; i++) {  // 循环删除所有的子文件及子文件夹
            // 删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            } else {  // 删除子文件夹
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            }
        }

        if (!flag) {
            System.out.println("删除失败");
            return false;
        }
        // 最后删除当前文件夹
        if (directoryFile.delete()) {
            System.out.println("删除成功：" + directory);
            return true;
        } else {
            System.out.println("删除失败：" + directory);
            return false;
        }
    }
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.isFile() && file.exists()) {
            file.delete();
            return true;
        } else {
            return false;
        }
    }

}
