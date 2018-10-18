package com.zeling.wa.ui;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zeling.wa.dao.CommonDao;
import com.zeling.wa.utils.HttpUtils;
import com.zeling.wa.utils.SecretUtils;
import com.zeling.wa.vo.ZlOrderVO;

/**
 * 微信辅助
 * 
 * @author chenbd 2018年10月9日
 */
public class WechatAssist extends JFrame {

	private static final long serialVersionUID = -627984564109834680L;
	
	private static final String ORDER_BASE_URL = "http://weixin.3f2mt.cn/weixin/tj";
	private static final String QUERY_BASE_URL = "http://weixin.3f2mt.cn/weixin/cx";
	
	
	public WechatAssist() {
		init();
	}
	
	private void init() {
		UIManager.put("Button.font", new Font("微软雅黑", Font.PLAIN, 24));
		UIManager.put("Label.font", new Font("微软雅黑", Font.PLAIN, 24));
		UIManager.put("TextField.font", new Font("微软雅黑", Font.PLAIN, 24));
		UIManager.put("List.font", new Font("微软雅黑", Font.PLAIN, 20));
		UIManager.put("TextArea.font", new Font("微软雅黑", Font.PLAIN, 24));
		
		this.setLayout(new GridLayout(1, 2, 10, 10));
		this.add(orderInit());
		this.add(recordsInit());

		this.setSize(1400, 800);
		this.setTitle("飞翔微信辅助招代理");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	private JPanel recordsInit() {
		JPanel recordPanel = new JPanel();
		recordPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		BoxLayout boxLayout = new BoxLayout(recordPanel, BoxLayout.Y_AXIS);
		recordPanel.setLayout(boxLayout);
		
		// 刷新下单记录
		JButton order = new JButton("刷新记录");
		order.setAlignmentX(1);
		
		// 删除下单记录
		JButton delete = new JButton("删除记录");
		delete.setAlignmentX(0);
		
		JPanel buttonPanel = new JPanel();
		BoxLayout boxLayout2 = new BoxLayout(buttonPanel, BoxLayout.X_AXIS);
		buttonPanel.setLayout(boxLayout2);
		buttonPanel.add(order);
		buttonPanel.add(delete);
		
		recordPanel.add(buttonPanel);	
		
		
		JScrollPane recordScroll = new JScrollPane();
		recordScroll.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
		JList<String> list = new JList<>();
		list.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
		list.setCellRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.setColor(Color.LIGHT_GRAY);
				g.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
			}
		});
		
		
		// 刷新
		order.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				refresh();
				list.setListData(getListData());
			}
		});
		
		// 删除
		delete.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				delete();
				list.setListData(getListData());
			}
		});
		
		refresh();
		list.setListData(getListData());
		recordScroll.setViewportView(list);
		
		recordPanel.add(recordScroll);
		
		// 黑框
		JPanel blackLine = new JPanel(new GridLayout(1, 1));
		blackLine.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 3));
		blackLine.add(recordPanel);
				
		return blackLine;
	}
	
	/**
	 * 获取最新数据
	 * 
	 * @return
	 */
	private Vector<String> getListData() {
		List<Map<String, String>> zlOrderVOs = getOrderDatas();
		Vector<String> values = new Vector<>();
		if (zlOrderVOs != null) {
			for (Map<String, String> map : zlOrderVOs) {
				String vstatus = map.get(ZlOrderVO.VSTATUS);
				String msg = null;
				if ("Y".equals(vstatus)) {
					msg = "辅助成功";
				} else if ("N".equals(vstatus)) {
					msg = "辅助失败";
				} else if ("Z".equals(vstatus)) {
					msg = "等待结果";
				} else {
					msg = "数据错误";
				}
				values.add(map.get(ZlOrderVO.VPHONE) + ";订单号:" + map.get(ZlOrderVO.VORDER_ID) + ";" + msg);
			}
		}
		return values;
	}
	
	/**
	 * 删除已经完成的订单
	 */
	private void delete() {
		List<Map<String, String>> zlOrderVOS = getOrderDatas();
		if (zlOrderVOS == null) {
			return;
		}
		Map<String, String> params = new HashMap<>();
		for (Map<String, String> map : zlOrderVOS) {
			String vstatus = map.get(ZlOrderVO.VSTATUS);
			if ("Z".equals(vstatus)) {
				continue;
			}
			// 删除动作
			params.put(ZlOrderVO.HID, map.get(ZlOrderVO.HID));
			try {
				CommonDao.deleteH2(ZlOrderVO.TABLE_NAME, params);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			params.clear();
		}
	}
	
	/**
	 * 获取订单信息
	 * 
	 * @return
	 */
	private List<Map<String, String>> getOrderDatas() {
		List<Map<String, String>> zlOrderVOs = null;
		try {
			zlOrderVOs = CommonDao.selectH2(ZlOrderVO.TABLE_NAME, new String[] { ZlOrderVO.HID, ZlOrderVO.VPHONE,
					ZlOrderVO.VORDER_ID, ZlOrderVO.VSTATUS, ZlOrderVO.VSECRET_KEY }, null);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return zlOrderVOs;
	}
	
	/**
	 * 刷新
	 */
	private void refresh() {
		List<Map<String, String>> zlOrderVOS = getOrderDatas();
		if (zlOrderVOS == null) {
			return;
		}
		Map<String, String> params = new HashMap<>();
		for (Map<String, String> map : zlOrderVOS) {
			String vstatus = map.get(ZlOrderVO.VSTATUS);
			if (!"Z".equals(vstatus)) {
				continue;
			}
			String vorder_id = map.get(ZlOrderVO.VORDER_ID);
			String vsecret_key = map.get(ZlOrderVO.VSECRET_KEY);
			String queryUrl = QUERY_BASE_URL + "?my=" + vsecret_key + "&id=" + vorder_id;
			JSONObject result = JSON.parseObject(HttpUtils.doGet(queryUrl));
			if (!new Integer(1).equals(result.getInteger("sts"))) {
				continue;
			}
			Integer sts = result.getJSONObject("d").getInteger("sts");
			if (new Integer(0).equals(sts)) {
				vstatus = "N";
			} else if (new Integer(1).equals(sts)) {
				vstatus = "Y";
			}
			if ("Z".equals(vstatus)) {
				continue;
			}
			params.put(ZlOrderVO.HID, map.get(ZlOrderVO.HID));
			try {
				CommonDao.updateH2(ZlOrderVO.TABLE_NAME, new String[] { ZlOrderVO.VSTATUS }, new String[] { vstatus }, params);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			params.clear();
		}
	}

	private JPanel orderInit() {
		// 个人密钥
//		JLabel secretKeyName = new JLabel("密钥");
//		secretKeyName.setHorizontalAlignment(JLabel.RIGHT);
//		JTextField secretKey = new JTextField(20);
//		JPanel secretKeyPanel = new JPanel(new GridLayout(1, 2, 15, 15));
//		secretKeyPanel.add(secretKeyName);
//		secretKeyPanel.add(secretKey);
		
		
		JButton secretKeyName = new JButton("设置密钥");
		JPanel settingSecretPanel = new JPanel();
		BoxLayout boxLayout = new BoxLayout(settingSecretPanel, BoxLayout.LINE_AXIS);
		settingSecretPanel.setLayout(boxLayout);
		settingSecretPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		settingSecretPanel.add(secretKeyName);
		
		JTextArea secretKey = new JTextArea();
		secretKey.setLineWrap(true);
		JPanel secretKeyPanel = new JPanel(new GridLayout(1, 2, 15, 15));
		secretKeyPanel.add(settingSecretPanel);
		secretKeyPanel.add(secretKey);
		
		// 密钥
		String secret = SecretUtils.getSecret();
		if (secret != null && !secret.trim().equals("")) {
			secretKeyName.setText("密钥");
			secretKeyName.setEnabled(false);
			secretKey.setText(secret);
			secretKey.setEnabled(false);
		}
		
		// 二维码图片
		JLabel qRCodeImgName = new JLabel("二维码图片");
		qRCodeImgName.setHorizontalAlignment(JLabel.RIGHT);
		JTextArea qRCodeImg = new JTextArea();
		qRCodeImg.setLineWrap(true);
		JPanel qRCodeImgPanel = new JPanel(new GridLayout(1, 2, 15, 15));
		qRCodeImgPanel.add(qRCodeImgName);
		qRCodeImgPanel.add(qRCodeImg);
		
		// 二维码链接
		JLabel qRCodeLinkName = new JLabel("二维码链接");
		qRCodeLinkName.setHorizontalAlignment(JLabel.RIGHT);
		JTextArea qRCodeLink = new JTextArea();
		qRCodeLink.setLineWrap(true);
		JPanel qRCodeLinkPanel = new JPanel(new GridLayout(1, 2, 15, 15));
		qRCodeLinkPanel.add(qRCodeLinkName);
		qRCodeLinkPanel.add(qRCodeLink);
		
		// 手机号
		JLabel phoneNumName = new JLabel("手机号");
		phoneNumName.setHorizontalAlignment(JLabel.RIGHT);
		JTextField phoneNum = new JTextField(20);
		JPanel phoneNumPanel = new JPanel(new GridLayout(1, 2, 15, 15));
		phoneNumPanel.add(phoneNumName);
		phoneNumPanel.add(phoneNum);
		
		// 下单
		JButton order = new JButton("下单");
		JPanel orderPanel = new JPanel();
		orderPanel.add(order);
		
		// 结果
//		JTextField result = new JTextField(20);
//		JPanel resultPanel = new JPanel(new GridLayout(1, 1, 15, 15));
//		resultPanel.add(result);
		
		JTextArea result = new JTextArea();
		result.setLineWrap(true);
		JPanel resultPanel = new JPanel(new GridLayout(1, 1, 15, 15));
		resultPanel.add(result);
		
		order.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				StringBuilder url = new StringBuilder(ORDER_BASE_URL);
				String secretKeyValue = secretKey.getText();
				if (secretKeyValue == null || secretKeyValue.trim().equals("")) {
					result.setText("请输入密钥");
					return;
				}
				url.append("?my=" + secretKeyValue.trim());
				String phoneValue = phoneNum.getText();
				if (phoneValue == null || phoneValue.trim().equals("")) {
					result.setText("请输入手机号");
					return;
				}
				url.append("&sjh=" + phoneValue.trim());
				String qRCodeImgValue = qRCodeImg.getText();
				if (qRCodeImgValue != null && !qRCodeImgValue.trim().equals("")) {
					url.append("&tupian=" + qRCodeImgValue.trim());
				}
				String qRCodeLinkValue = qRCodeLink.getText();
				if (qRCodeLinkValue != null && !qRCodeLinkValue.trim().equals("")) {
					url.append("&lianjie=" + qRCodeLinkValue.trim());
				}
				String response = HttpUtils.doGet(url.toString());
				JSONObject jsonObject = JSON.parseObject(response);
				Integer sts = jsonObject.getInteger("sts");
				if (sts != null && sts == 0) {
					result.setText("下单失败，请检查密钥、手机号以及二维码是否正确");
					return;
				}
				JSONObject d = jsonObject.getJSONObject("d");
				String id = d.getString("id");
				result.setText("下单成功！\nid:" + id);
				try {
					CommonDao.insertH2(ZlOrderVO.TABLE_NAME,
							new String[] { ZlOrderVO.VPHONE, ZlOrderVO.VORDER_ID, ZlOrderVO.VSTATUS,
									ZlOrderVO.VSECRET_KEY },
							new String[] { phoneValue.trim(), id.trim(), "Z", secretKeyValue.trim() });
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		secretKeyName.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String secretKeyValue = secretKey.getText();
				if (secretKeyValue == null || secretKeyValue.trim().equals("")) {
					result.setText("请输入密钥");
					return;
				}
				SecretUtils.saveSecret(secretKeyValue.trim());
				
				// 禁用设置密钥的功能
				secretKeyName.setText("密钥");
				secretKeyName.setEnabled(false);
				secretKey.setText(secretKeyValue.trim());
				secretKey.setEnabled(false);
			}
		});
		
		// 下单页面
		JPanel orderMain = new JPanel(new GridLayout(6, 1, 15, 15));
		orderMain.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		orderMain.add(secretKeyPanel);
		orderMain.add(qRCodeLinkPanel);
		orderMain.add(qRCodeImgPanel);
		orderMain.add(phoneNumPanel);
		orderMain.add(orderPanel);
		orderMain.add(resultPanel);
		
		// 黑框
		JPanel blackLine = new JPanel(new GridLayout(1, 1));
		blackLine.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 3));
		blackLine.add(orderMain);
		
		return blackLine;
	}

}
