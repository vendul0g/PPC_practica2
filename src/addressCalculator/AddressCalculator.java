package addressCalculator;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class AddressCalculator {
	public AddressCalculator() {
		
	}
	
	public static InetAddress getWloAddress() {
		String interfaceName = "wlo1"; // Replace with your specific interface name

        try {
            NetworkInterface networkInterface = NetworkInterface.getByName(interfaceName);
            Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();

            while (addresses.hasMoreElements()) {
                InetAddress address = addresses.nextElement();
                return toBroadcast(address);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
	}
	
	private static InetAddress toBroadcast(InetAddress address) {
		byte[] aux = address.getAddress();
		System.out.println(aux[3]);
		try {
			return InetAddress.getByAddress(aux);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
