package ywcai.ls.desk.cfg;

public class ResultCode {


	/*
	 * event notify
	 */
    public static final int json_type_notify_turn_on=6001;//turn_on
    public static final int json_type_notify_turn_off=6002;//turn_off
    public static final int json_type_notify_link_up=6667;//link_up
    public static final int json_type_notify_link_down=6668;//link_down
    public static final int json_type_notify_link_fail=6669;//link_fail
    
	/*
	 * response client
	 */
	public static final int json_type_validate_success = 7706;//device turn online success
	public static final int json_type_validate_fail = 7701;//device turn online success
	
	/*
	 * recive client
	 */
	public static final int json_type_check_token = 8000;
	public static final int json_type_request_link = 8001;
	public static final int json_type_cut_link = 8002;
	
	/*
	 * transfer data
	 */
	public static final int json_type_control_cmd = 9000;
	
	
	
}
