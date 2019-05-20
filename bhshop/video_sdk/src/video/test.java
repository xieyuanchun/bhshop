package video;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.ByteByReference;


public class test {

	private static NativeLong m_lRealHandle = new NativeLong(-1);// 预览句柄

	static HCNetSDK hCNetSDK = HCNetSDK.INSTANCE;

	private boolean m_bLightOn;// 开启灯光
	private boolean m_bFanOn;// 开启风扇
	private boolean m_bHeaterOn;// 开启加热
	private boolean m_bWiperOn;// 开启雨刷

	/*************************************************
	 * 函数: JFramePTZControl 函数描述: 构造函数 Creates new form JFramePTZControl
	 *************************************************/
	public test(NativeLong lRealHandle) {
		// m_lRealHandle = m_lRealHandle;
		m_bLightOn = false;
		m_bWiperOn = false;
		m_bFanOn = false;
		m_bHeaterOn = false;
		//fRealDataCallBack = new FRealDataCallBack();;// 预览回调函数实现
	}

	/*************************************************
	 * 函数名: PTZControlAll 函数描述: 云台控制函数 输入参数: lRealHandle: 预览句柄 iPTZCommand: PTZ控制命令
	 * iStop: 开始或是停止操作 输出参数: 返回值:
	 *************************************************/
	private static void PTZControlAll(NativeLong lRealHandle, int iPTZCommand, int iStop) {
		
		boolean ret;
        ret = hCNetSDK.NET_DVR_PTZControl(lRealHandle, iPTZCommand, iStop);
		System.out.println("...................");
		if (!ret) {
			
			System.out.println("失败");
			return;
		}
		
	}

	/*************************************************
	 * 函数: "灯光"按钮 双击响应函数 函数描述: 云台控制函数 打开/关闭灯光
	 *************************************************/
	private void jButtonLightActionPerformed() {// GEN-FIRST:event_jButtonLightActionPerformed
		if (!m_bLightOn) {
			hCNetSDK.NET_DVR_PTZControl(m_lRealHandle, HCNetSDK.LIGHT_PWRON, 0);
			// jButtonLight.setText("关灯");
			m_bLightOn = true;
		} else {
			hCNetSDK.NET_DVR_PTZControl(m_lRealHandle, HCNetSDK.LIGHT_PWRON, 1);
			// jButtonLight.setText("灯光");
			m_bLightOn = false;
		}
	}

	/*************************************************
	 * 函数: "风扇"按钮 双击响应函数 函数描述: 云台控制函数 开始/停止 风扇
	 *************************************************/
	private void jButtonFanPwronActionPerformed() {// GEN-FIRST:event_jButtonFanPwronActionPerformed
		if (!m_bFanOn) {
			hCNetSDK.NET_DVR_PTZControl(m_lRealHandle, HCNetSDK.FAN_PWRON, 0);
			// jButtonFanPwron.setText("停风");
			m_bFanOn = true;
		} else {
			hCNetSDK.NET_DVR_PTZControl(m_lRealHandle, HCNetSDK.FAN_PWRON, 1);
			// jButtonFanPwron.setText("风扇");
			m_bFanOn = false;
		}
	}

	/*************************************************
	 * 函数: "加热"按钮 双击响应函数 函数描述: 云台控制函数 开始/停止 加热
	 *************************************************/
	private void jButtonHeaterActionPerformed() {// GEN-FIRST:event_jButtonHeaterActionPerformed
		if (!m_bHeaterOn) {
			hCNetSDK.NET_DVR_PTZControl(m_lRealHandle, HCNetSDK.HEATER_PWRON, 0);
			// jButtonHeater.setText("停止");
			m_bHeaterOn = true;
		} else {
			hCNetSDK.NET_DVR_PTZControl(m_lRealHandle, HCNetSDK.HEATER_PWRON, 1);
			// jButtonHeater.setText("加热");
			m_bHeaterOn = false;
		}
	}

	/*************************************************
	 * 函数: "雨刷"按钮 双击响应函数 函数描述: 云台控制函数 开始/停止雨刷
	 *************************************************/
	private void jButtonWiperPwronActionPerformed() {// GEN-FIRST:event_jButtonWiperPwronActionPerformed
		if (!m_bWiperOn) {
			hCNetSDK.NET_DVR_PTZControl(m_lRealHandle, HCNetSDK.WIPER_PWRON, 0);
			// jButtonWiperPwron.setText("雨停");
			m_bWiperOn = true;
		} else {
			hCNetSDK.NET_DVR_PTZControl(m_lRealHandle, HCNetSDK.WIPER_PWRON, 1);
			// jButtonWiperPwron.setText("雨刷");
			m_bWiperOn = false;
		}
	}

	// 左上

	public void mousePressed() {
		jButtonLeftUpMousePressed();
	}

	public void mouseReleased() {
		jButtonLeftUpMouseReleased();
	}

	// 左

	public void mousePressed1() {
		jButtonLeftMousePressed();
	}

	public void mouseReleased2() {
		jButtonLeftMouseReleased();
	}

	// 左下
	public void mousePressed3() {
		jButtonLeftDownMousePressed();
	}

	public void mouseReleased5() {
		jButtonLeftDownMouseReleased();
	}

	// 上

	public void mousePressed6() {
		jButtonUpMousePressed();
	}

	public void mouseReleased7() {
		jButtonUpMouseReleased();
	}

	// 下

	public void mousePressed8() {
		jButtondownMousePressed();
	}

	public void mouseReleased9() {
		jButtondownMouseReleased();
	}

	// 右上

	public void mousePressed10() {
		jButtonRightUpMousePressed();
	}

	public void mouseReleased11() {
		jButtonRightUpMouseReleased();
	}

	// 右

	public void mousePressed12() {
		jButtonRightMousePressed();
	}

	public void mouseReleased13() {
		jButtonRightMouseReleased();
	}

	// 右下

	public void mousePressed15() {
		jButtonRightDownMousePressed();
	}

	public void mouseReleased16() {
		jButtonRightDownMouseReleased();
	}

	// 伸

	public void mousePressed17() {
		jButton1ZoomOutMousePressed();
	}

	public void mouseReleased18() {
		jButton1ZoomOutMouseReleased();
	}

	// 缩

	public void mousePressed19() {
		jButtonZoomInMousePressed();
	}

	public void mouseReleased20() {
		jButtonZoomInMouseReleased();
	}

	// 近

	public void mousePressed21() {
		jButton1FocusNearMousePressed();
	}

	public void mouseReleased22() {
		jButton1FocusNearMouseReleased();
	}

	// 远
	public void mousePressed23() {
		jButtonFocusFarMousePressed();
	}

	public void mouseReleased24() {
		jButtonFocusFarMouseReleased();
	}

	// 大
	public void mousePressed25() {
		jButton1IrisOpenMousePressed();
	}

	public void mouseReleased26() {
		jButton1IrisOpenMouseReleased();
	}

	// 小
	public void mousePressed27() {
		jButton1IrisCloseMousePressed();
	}

	public void mouseReleased28() {
		jButton1IrisCloseMouseReleased();
	}

	// 灯光
	public void actionPerformed() {
		jButtonLightActionPerformed();
	}

	// 风扇
	public void actionPerformed2() {
		jButtonFanPwronActionPerformed();
	}

	// 加热
	public void actionPerformed3() {
		jButtonHeaterActionPerformed();
	}

	// 雨刷
	public void actionPerformed5() {
		jButtonWiperPwronActionPerformed();
	}

	/**
	 * 左上
	 */
	private void jButtonLeftUpMousePressed() {
		PTZControlAll(m_lRealHandle, HCNetSDK.UP_LEFT, 0);
	}

	private void jButtonLeftUpMouseReleased() {
		PTZControlAll(m_lRealHandle, HCNetSDK.UP_LEFT, 1);
	}

	/**
	 * 左
	 */
	private void jButtonLeftMousePressed() {
		PTZControlAll(m_lRealHandle, HCNetSDK.PAN_LEFT, 0);
	}

	private void jButtonLeftMouseReleased() {
		PTZControlAll(m_lRealHandle, HCNetSDK.PAN_LEFT, 1);
	}

	/**
	 * 左下
	 */
	private void jButtonLeftDownMousePressed() {
		PTZControlAll(m_lRealHandle, HCNetSDK.DOWN_LEFT, 0);
	}

	private void jButtonLeftDownMouseReleased() {
		PTZControlAll(m_lRealHandle, HCNetSDK.DOWN_LEFT, 1);
	}

	/**
	 * 上
	 * 
	 * @param evt
	 */
	private void jButtonUpMousePressed() {
		PTZControlAll(m_lRealHandle, HCNetSDK.TILT_UP, 0);
	}

	private void jButtonUpMouseReleased() {
		PTZControlAll(m_lRealHandle, HCNetSDK.TILT_UP, 1);
	}

	/**
	 * 下
	 * 
	 * @param evt
	 */
	private void jButtondownMousePressed() {
		PTZControlAll(m_lRealHandle, HCNetSDK.TILT_DOWN, 0);
	}

	private void jButtondownMouseReleased() {
		PTZControlAll(m_lRealHandle, HCNetSDK.TILT_DOWN, 1);
	}

	/**
	 * 右上
	 * 
	 * @param evt
	 */
	private void jButtonRightUpMousePressed() {// GEN-FIRST:event_jButtonRightUpMousePressed
		PTZControlAll(m_lRealHandle, HCNetSDK.TILT_DOWN, 0);
	}// GEN-LAST:event_jButtonRightUpMousePressed

	private void jButtonRightUpMouseReleased() {// GEN-FIRST:event_jButtonRightUpMouseReleased
		PTZControlAll(m_lRealHandle, HCNetSDK.TILT_DOWN, 1);
	}// GEN-LAST:event_jButtonRightUpMouseReleased

	/**
	 * 右
	 * 
	 * @param evt
	 */
	private void jButtonRightMousePressed() {
		PTZControlAll(m_lRealHandle, HCNetSDK.PAN_RIGHT, 0);
	}

	private void jButtonRightMouseReleased() {
		PTZControlAll(m_lRealHandle, HCNetSDK.PAN_RIGHT, 1);
	}

	/**
	 * 右下
	 * 
	 * @param evt
	 */
	private void jButtonRightDownMousePressed() {
		PTZControlAll(m_lRealHandle, HCNetSDK.DOWN_RIGHT, 0);
	}

	private void jButtonRightDownMouseReleased() {
		PTZControlAll(m_lRealHandle, HCNetSDK.DOWN_RIGHT, 1);
	}

	/**
	 * 伸
	 * 
	 * @param evt
	 */
	private void jButton1ZoomOutMousePressed() {
		PTZControlAll(m_lRealHandle, HCNetSDK.ZOOM_OUT, 0);
	}

	private void jButton1ZoomOutMouseReleased() {
		PTZControlAll(m_lRealHandle, HCNetSDK.ZOOM_OUT, 1);
	}

	/**
	 * 缩
	 * 
	 * @param evt
	 */
	private void jButtonZoomInMousePressed() {
		PTZControlAll(m_lRealHandle, HCNetSDK.ZOOM_IN, 0);
	}

	private void jButtonZoomInMouseReleased() {
		PTZControlAll(m_lRealHandle, HCNetSDK.ZOOM_IN, 1);
	}

	/**
	 * 近
	 * 
	 * @param evt
	 */
	private void jButton1FocusNearMousePressed() {
		PTZControlAll(m_lRealHandle, HCNetSDK.FOCUS_NEAR, 0);
	}

	private void jButton1FocusNearMouseReleased() {
		PTZControlAll(m_lRealHandle, HCNetSDK.FOCUS_NEAR, 1);
	}

	/**
	 * 远
	 * 
	 * @param evt
	 */
	private void jButtonFocusFarMousePressed() {
		PTZControlAll(m_lRealHandle, HCNetSDK.FOCUS_FAR, 0);
	}

	private void jButtonFocusFarMouseReleased() {
		PTZControlAll(m_lRealHandle, HCNetSDK.FOCUS_FAR, 1);
	}

	/**
	 * 大
	 * 
	 * @param evt
	 */
	private void jButton1IrisOpenMousePressed() {
		PTZControlAll(m_lRealHandle, HCNetSDK.IRIS_OPEN, 0);
	}

	private void jButton1IrisOpenMouseReleased() {
		PTZControlAll(m_lRealHandle, HCNetSDK.IRIS_OPEN, 1);
	}

	/**
	 * 小
	 * 
	 * @param evt
	 */
	private void jButton1IrisCloseMousePressed() {
		PTZControlAll(m_lRealHandle, HCNetSDK.IRIS_CLOSE, 0);
	}

	private void jButton1IrisCloseMouseReleased() {
		PTZControlAll(m_lRealHandle, HCNetSDK.IRIS_CLOSE, 1);
	}

	

	public static void main(String param[]) {

		NativeLong lUserID;// 用户句柄
		HCNetSDK.NET_DVR_DEVICEINFO_V30 m_strDeviceInfo  = new HCNetSDK.NET_DVR_DEVICEINFO_V30();// 设备信息
		m_strDeviceInfo = new HCNetSDK.NET_DVR_DEVICEINFO_V30();
		lUserID = hCNetSDK.NET_DVR_Login_V30("192.168.9.64", (short) 8000, "admin", "zy123456789", m_strDeviceInfo);
		int iStop0 = 0;
		int iStop = 1;
		NativeLong lPreviewHandle = new NativeLong(-1);

		HCNetSDK.NET_DVR_CLIENTINFO m_strClientInfo = new HCNetSDK.NET_DVR_CLIENTINFO();// 用户参数

		m_strClientInfo.hPlayWnd = null;
		FRealDataCallBack fRealDataCallBack = new FRealDataCallBack();// 预览回调函数实现
		lPreviewHandle = hCNetSDK.NET_DVR_RealPlay_V30(lUserID, m_strClientInfo, fRealDataCallBack, null, true);
        
		boolean ret = hCNetSDK.NET_DVR_PTZControl(lPreviewHandle, HCNetSDK.PAN_RIGHT, iStop0);
		boolean ret2 = hCNetSDK.NET_DVR_PTZControl(lPreviewHandle, HCNetSDK.PAN_RIGHT, iStop);
		System.out.println(">>>>>>>>>>>>>>>>>>>");
		if (!ret||!ret2) {	
			System.out.println("失败");
		}else {
			System.out.println("成功");
		}

	}

	/******************************************************************************
	 * 内部类: FRealDataCallBack 实现预览回调数据
	 ******************************************************************************/
	static class FRealDataCallBack implements HCNetSDK.FRealDataCallBack_V30 {
		// 预览回调
		public void invoke(NativeLong lRealHandle, int dwDataType, ByteByReference pBuffer, int dwBufSize,
				Pointer pUser) {

		}
	}

}
