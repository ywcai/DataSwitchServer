package ywcai.ls.desk.control;




import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ControlServer {
	public static Logger logger;
	public static void main(String[] args) {
		PropertyConfigurator.configure("log/log4j.properties");
		logger =LoggerFactory.getLogger(ControlServer.class);
		ControlBus controlBus=new ControlBus();
		controlBus.Init();
	}
}
