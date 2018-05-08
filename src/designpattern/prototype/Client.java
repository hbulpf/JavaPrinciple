package designpattern.prototype;

import java.util.Random;

import designpattern.prototype.personaMail.AdvTemplate;
import designpattern.prototype.personaMail.Mail;

/**
 * prototype pattern
 * @author foreverlpficloud.com
 *
 */
public class Client {

	//发送账单的数量，从数据库中得到
	private static int MAX_COUNT=6;
	public static void main(String args[]) {
		int i = 0;
		Mail mail = new Mail(new AdvTemplate());
		mail.setTail("XX银行版权所有");
		while(i<MAX_COUNT) {
			Mail cloneMail = mail.clone();
			cloneMail.setAppellation(getRandString(5)+"先生/女生");
			cloneMail.setReceiver(getRandString(5)+"@"+getRandString(8)+".com");			
			sendMail(cloneMail);
			i++;		
		}
	}
	//发送邮件
	public static void sendMail(Mail mail) {
		System.out.println("标题："+mail.getSubject()+"\t内容："+mail.getContext()+"\tAppellation："+mail.getAppellation()+"\t收件人："+mail.getReceiver()+"\t......发送成功。");
	}
	//获得指定长度的随机字符串
	public static String getRandString(int maxLength) {
		String source = "abcdefghijklmnopqrskuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		StringBuffer sb = new StringBuffer();
		Random rand = new Random();
		for(int i=0;i<maxLength;i++) {
			sb.append(source.charAt(rand.nextInt(source.length())));
		}
		return sb.toString();
	}
}
