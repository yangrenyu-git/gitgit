package com.javaxxz.core.toolbox.kit;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import com.javaxxz.core.exception.ToolBoxException;
import com.javaxxz.core.toolbox.support.FieldValidator;


public class NetKit {
	public final static String LOCAL_IP = "127.0.0.1";
	

	public static String longToIpv4(long longIP) {
		StringBuffer sb = new StringBuffer();
		// 直接右移24位
		sb.append(String.valueOf(longIP >>> 24));
		sb.append(".");
		// 将高8位置0，然后右移16位
		sb.append(String.valueOf((longIP & 0x00FFFFFF) >>> 16));
		sb.append(".");
		sb.append(String.valueOf((longIP & 0x0000FFFF) >>> 8));
		sb.append(".");
		sb.append(String.valueOf(longIP & 0x000000FF));
		return sb.toString();
	}


	public static long ipv4ToLong(String strIP) {
		if (FieldValidator.isIpv4(strIP)) {
			long[] ip = new long[4];
			// 先找到IP地址字符串中.的位置
			int position1 = strIP.indexOf(".");
			int position2 = strIP.indexOf(".", position1 + 1);
			int position3 = strIP.indexOf(".", position2 + 1);
			// 将每个.之间的字符串转换成整型
			ip[0] = Long.parseLong(strIP.substring(0, position1));
			ip[1] = Long.parseLong(strIP.substring(position1 + 1, position2));
			ip[2] = Long.parseLong(strIP.substring(position2 + 1, position3));
			ip[3] = Long.parseLong(strIP.substring(position3 + 1));
			return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
		}
		return 0;
	}
	

	public static boolean isUsableLocalPort(int port) {
		if (! isValidPort(port)) {
			// 给定的IP未在指定端口范围中
			return false;
		}
		try {
			new Socket(LOCAL_IP, port).close();
			// socket链接正常，说明这个端口正在使用
			return false;
		} catch (Exception e) {
			return true;
		}
	}
	

	public static boolean isValidPort(int port) {
		//有效端口是0～65535
		return port >= 0 && port <= 0xFFFF;
	}
	

	public static boolean isInnerIP(String ipAddress) {
		boolean isInnerIp = false;
		long ipNum = NetKit.ipv4ToLong(ipAddress);
		
		long aBegin = NetKit.ipv4ToLong("10.0.0.0");
		long aEnd = NetKit.ipv4ToLong("10.255.255.255");
		
		long bBegin = NetKit.ipv4ToLong("172.16.0.0");
		long bEnd = NetKit.ipv4ToLong("172.31.255.255");
		
		long cBegin = NetKit.ipv4ToLong("192.168.0.0");
		long cEnd = NetKit.ipv4ToLong("192.168.255.255");
		
		isInnerIp = isInner(ipNum, aBegin, aEnd) || isInner(ipNum, bBegin, bEnd) || isInner(ipNum, cBegin, cEnd) || ipAddress.equals(LOCAL_IP);
		return isInnerIp;
	}
	

	public static Set<String> localIpv4s() {
		Enumeration<NetworkInterface> networkInterfaces = null;
		try {
			networkInterfaces = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			throw new ToolBoxException(e.getMessage(), e);
		}
		
		if(networkInterfaces == null) {
			throw new ToolBoxException("Get network interface error!");
		}
		
		final HashSet<String> ipSet = new HashSet<String>();
		
		while(networkInterfaces.hasMoreElements()) {
			final NetworkInterface networkInterface = networkInterfaces.nextElement();
			final Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
			while(inetAddresses.hasMoreElements()) {
				final InetAddress inetAddress = inetAddresses.nextElement();
				if(inetAddress != null && inetAddress instanceof Inet4Address) {
					ipSet.add(inetAddress.getHostAddress());
				}
			}
		}
		
		return ipSet;
	}
	

	public static String toAbsoluteUrl(String absoluteBasePath, String relativePath) {
		try {
			URL absoluteUrl = new URL(absoluteBasePath);
			return new URL(absoluteUrl ,relativePath).toString();
		} catch (Exception e) {
			throw new ToolBoxException(StrKit.format("To absolute url [{}] base [{}] error!", relativePath, absoluteBasePath), e);
		}
	}
	

	public static String hideIpPart(String ip) {
		return new StringBuffer(ip.length())
			.append(ip.substring(0, ip.lastIndexOf(".") + 1))
			.append("*").toString();
	}
	

	public static String hideIpPart(long ip) {
		return hideIpPart(longToIpv4(ip));
	}
	

	public static InetSocketAddress buildInetSocketAddress(String host, int defaultPort) {
		if(StrKit.isBlank(host)) {
			host = LOCAL_IP;
		}
		
		String destHost = null;
		int port = 0;
		int index = host.indexOf(":");
		if (index != -1) {
			// host:port形式
			destHost = host.substring(0, index);
			port = Integer.parseInt(host.substring(index + 1));
		} else {
			destHost = host;
			port = defaultPort;
		}
		
		return new InetSocketAddress(destHost, port);
	}
	

	public static String getLocalMac() {
		InetAddress ia = null;
		byte[] mac = null;
		try {
			ia = InetAddress.getLocalHost();
			mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//System.out.println("mac数组长度：" + mac.length);
		StringBuffer sb = new StringBuffer("");
		for(int i=0; i<mac.length; i++) {
			if(i!=0) {
				sb.append("-");
			}
			int temp = mac[i]&0xff;
			String str = Integer.toHexString(temp);
			//System.out.println("每8位:" + str);
			if(str.length()==1) {
				sb.append("0"+str);
			}else {
				sb.append(str);
			}
		}
		LogKit.info("本机MAC地址:" + sb.toString().toUpperCase());
		return sb.toString().toUpperCase();
	}
	
	//----------------------------------------------------------------------------------------- Private method start

	private static boolean isInner(long userIp, long begin, long end) {
		return (userIp >= begin) && (userIp <= end);
	}
	//----------------------------------------------------------------------------------------- Private method end
}
