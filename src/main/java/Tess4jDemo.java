import java.io.File;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

/**
 * @author zhouxinghang
 * @date 2021/12/6
 */
public class Tess4jDemo {
    public static void main(String[] args) {

        //验证码图片存储地址
        File imageFile = new File("/Users/zhouxinghang/Downloads/WX20211206-123616@2x.png");
        if(!imageFile.exists()){
            System.out.println("图片不存在");;
        }
        Tesseract tessreact = new Tesseract();
        tessreact.setDatapath("/opt/tessdata");
        tessreact.setLanguage("chi_sim");

        String result;
        try {
            result = "测验结果：" + tessreact.doOCR(imageFile);
            System.out.println(result);
        } catch (TesseractException e) {
            e.printStackTrace();
        }

    }
}
