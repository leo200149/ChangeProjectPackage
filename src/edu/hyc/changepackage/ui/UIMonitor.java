package edu.hyc.changepackage.ui;

import javafx.scene.control.TextInputControl;
import edu.hyc.changepackage.core.Monitor;
import edu.hyc.changepackage.util.LogUtil;

public class UIMonitor implements Monitor{
	
	private TextInputControl showResultBlock;
	
	private LogUtil logUtil;
	
	public UIMonitor(TextInputControl showResultBlock){
		this.showResultBlock = showResultBlock;
		logUtil = new LogUtil(getClass());
	}
	
	public void print(String message){
		if(showResultBlock.getText().length()>0){
			showResultBlock.setText(showResultBlock.getText()+"\n"+message);
		}else{
			showResultBlock.setText(message);
		}
		logUtil.doLog(message);
	}
	
	public void print(String[] messages){
		for(String message:messages){
			print(message);
		}
	}
}
