package application;

import java.net.URL;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import runner.ClientRunner;

public class MenuController implements Initializable {

	@FXML
	private MenuBar mainBar;

	@FXML
	private Menu User;

	@FXML
	private Menu File;

	@FXML
	private Menu Run;
	//User
	@FXML
	private MenuItem Log_in;

	@FXML
	private MenuItem Log_out;
	//File
	@FXML
	private MenuItem New;

	@FXML
	private MenuItem Open;

	@FXML
	private MenuItem Save;
	
	@FXML
	private MenuItem Version;
	
	//Run
	@FXML
	private MenuItem Execute;

	//EditArea
	@FXML
	private  TextArea ExitArea;
	
	//InputArea
	@FXML
	private TextArea InputArea;
	
	//OutputArea
	@FXML
	private TextArea OutputArea;

	@FXML
	private Label Condition;

	@FXML
	public  Label fileName;

	
	@FXML
	public static User user;

	@FXML
	public static boolean if_newfile=true;
	
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO 自动生成的方法存根

	}



	//登陆
	public void Log_in(){
		if(user==null){
			Main.showLogDislog();
			if(user!=null){
				Condition.setText(user.getUsername());
			}
		}
		else{
			Alert error=new Alert(Alert.AlertType.ERROR,"Please Log Out First");
			Button confirm=new Button();
			error.show();
			confirm.setOnAction((ActionEvent e)->{
				 error.close();
			});
		}
	}
	//登出
	public void Log_out(){
		if(user!=null){
			user=null;
			ExitArea.setText("");
			InputArea.setText("");
			OutputArea.setText("");
			fileName.setText("Filename");
			Alert error=new Alert(Alert.AlertType.INFORMATION,"Log Out Successed");
			Button confirm=new Button();
			error.show();
			confirm.setOnAction((ActionEvent e)->{
				 error.close();
			});
			Condition.setText("未登录");
			fileName.setText("");
			if_newfile=true;
		}

	}
	//运行
	public void Execute(){
		try {
			OutputArea.setText("");
			String result=ClientRunner.remoteHelper.getExecuteService().execute(ExitArea.getText(),InputArea.getText());
			OutputArea.setText(result);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}
	//新文件
	public void New(){
		if(user!=null){
			if_newfile=true;
			Main.showNewFileDialog();
			fileName.setText(user.getPomptFile());
			ExitArea.setText("");
			InputArea.setText("");
			OutputArea.setText("");
		}
		else{
			Alert error=new Alert(Alert.AlertType.INFORMATION,"Please Log in!");
		    Button confirm=new Button();
		    error.show();
		    confirm.setOnAction((ActionEvent e)->{
		    	error.close();
		    });
		}
	}

	//储存
	public void Save(){
		if(user!=null){
			try {
				boolean sign=ClientRunner.remoteHelper.getIOService().writeFilelist(user.getUsername(),user.getPomptFile(),if_newfile,ExitArea.getText());
				if(!sign){
					Alert error=new Alert(Alert.AlertType.WARNING,"File has existed!");
				    Button confirm=new Button();
				    error.show();
				    confirm.setOnAction((ActionEvent e)->{
				    	error.close();
				    });
				}
				else{
					Alert error=new Alert(Alert.AlertType.INFORMATION,"Save succeeded!");
				    Button confirm=new Button();
				    error.show();
				    confirm.setOnAction((ActionEvent e)->{
				    	error.close();
				    });
				    String list=ClientRunner.remoteHelper.getIOService().readFileList(MenuController.user.getUsername());
					MenuController.user.setList(list.split(" "));
				}
			} catch (RemoteException e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}
		else{
			Alert error=new Alert(Alert.AlertType.INFORMATION,"Please Log in!");
		    Button confirm=new Button();
		    error.show();
		    confirm.setOnAction((ActionEvent e)->{
		    	error.close();
		    });
		}
	}

	//打开
	public void Open(){
		if_newfile=false;
		if(user!=null){
			Main.showOpenDialog();
			ExitArea.setText(MenuController.user.getCode());
			fileName.setText(MenuController.user.getPomptFile());;
		}
		else{
			Alert error=new Alert(Alert.AlertType.INFORMATION,"Please Log in!");
		    Button confirm=new Button();
		    error.show();
		    confirm.setOnAction((ActionEvent e)->{
		    	error.close();
		    });
		}

	}
	
	//存储版本
	public void Version(){
		if(user!=null){
			try {
				java.util.Date now=new java.util.Date();
				SimpleDateFormat dateFormat=new SimpleDateFormat("yyMMddhhmm");
				String date = dateFormat.format( now ); 
				String temp=user.getPomptFile();
				if(user.getPomptFile().indexOf(")")>=0){
				temp=user.getPomptFile().substring(0, user.getPomptFile().indexOf("("));
				}
				String prompt_name=temp+"("+date+")";
				
				boolean sign=ClientRunner.remoteHelper.getIOService().writeFilelist(user.getUsername(),prompt_name,true,ExitArea.getText());
				if(sign){
					Alert error=new Alert(Alert.AlertType.INFORMATION,"此版本已保存！");
				    Button confirm=new Button();
				    error.show();
				    confirm.setOnAction((ActionEvent e)->{
				    	error.close();
				    });
				}
				
			} catch (RemoteException e) {
				// TODO: handle exception
			}
		}
		else{
			Alert error=new Alert(Alert.AlertType.INFORMATION,"Please Log in!");
		    Button confirm=new Button();
		    error.show();
		    confirm.setOnAction((ActionEvent e)->{
		    	error.close();
		    });
		}
	}

}
