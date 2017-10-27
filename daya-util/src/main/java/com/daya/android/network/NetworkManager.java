/*
 * Copyright 2013-present NHN Entertainment Corp. All rights Reserved.
 * NHN Entertainment PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @author shhong@nhnent.com
 */

package com.daya.android.network;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * Created by shhong on 2017. 6. 14..
 */

public final class NetworkManager {
    private static final String TAG = NetworkManager.class.getSimpleName();

    private NetworkManager() {}

    /**
     * Indicates whether network connectivity is possible.
     *
     * @param context   The Context for obtaining network information.
     * @return  true if the network is available, false otherwise
     */
    public static boolean isAvailable(Context context) {
        NetworkInfo networkInfo = getNetworkInfo(context);
        return (networkInfo != null && networkInfo.isAvailable());
    }

    /**
     * Indicates whether network connectivity exists
     * and it is possible to establish connections and pass data.
     *
     * @param context   The Context for obtaining network information.
     * @return  true if network connectivity exists, false otherwise.
     */
    public static boolean isConnected(Context context) {
        NetworkInfo networkInfo = getNetworkInfo(context);
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Indicates whether network connectivity exists or is in the process of being established.
     *
     * @param context   The Context for obtaining network information.
     * @return  true if network connectivity exists or is in the process of being established, false otherwise.
     */
    public static boolean isConnectedOrConnecting(Context context) {
        NetworkInfo networkInfo = getNetworkInfo(context);
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }

    /**
     * Reports the type of network.
     *
     * @param context   The Context for obtaining network information.
     * @return  The type of network.
     */
    public static int getType(Context context) {
        NetworkInfo networkInfo = getNetworkInfo(context);
        if (networkInfo == null) {
            return -1;
        }
        return networkInfo.getType();
    }

    /**
     * Return a human-readable name describe the type of the network, for example "WIFI" or "MOBILE".
     *
     * @param context   The Context for obtaining network information.
     * @return  the name of the network type
     */
    public static String getTypeName(Context context) {
        NetworkInfo networkInfo = getNetworkInfo(context);
        return (networkInfo != null) ? networkInfo.getTypeName() : null;
    }

    /**
     * Returns details about the currently active default data network.
     *
     * @param context   The Context for obtaining network information.
     * @return  a NetworkInfo object for the current default network or null if no default network is currently active
     */
    private static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return (connectivityManager != null) ? connectivityManager.getActiveNetworkInfo() : null;
    }

    /**
     * Gets the host name for IP address of the local host.
     *
     * @return the host name for IP address of the local host.
     */
    public static String getLocalHostName() {
        try {
            // ex) "localhost"
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Returns the IP address string.
     *
     * @return the IP address string.
     */
    public static String getHostAddress() {
        try {
            Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces();

            Log.d(TAG, "==============================================================");

            while (networkInterfaceEnumeration.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaceEnumeration.nextElement();

                Log.d(TAG, "--------------------------------------------------------------");
                Log.d(TAG, "# Interface Name: " + networkInterface.getDisplayName());
                Log.d(TAG, "\t- Is Loopback: " + networkInterface.isLoopback());
                Log.d(TAG, "\t- Is Virtual: " + networkInterface.isVirtual());
                Log.d(TAG, "\t- Is Up: " + networkInterface.isUp());

                if (networkInterface.isUp() && !networkInterface.isLoopback()) {
                    // 작동중(isUp)이고 루프백(isLoopback)이 아닌 경우..
                    Enumeration<InetAddress> inetAddressEnumeration = networkInterface.getInetAddresses();

                    while (inetAddressEnumeration.hasMoreElements()) {
                        InetAddress inetAddress = inetAddressEnumeration.nextElement();
                        String hostAddress = inetAddress.getHostAddress();

                        Log.d(TAG, "## Host Name: " + inetAddress.getHostName());
                        Log.d(TAG, "\t- Host Address: " + hostAddress);
                        Log.d(TAG, "\t- Is Loopback: " + inetAddress.isLoopbackAddress());
                        Log.d(TAG, "\t- Is Link Local Address: " + inetAddress.isLinkLocalAddress());
                        Log.d(TAG, "\t- Is Site Local Address: " + inetAddress.isSiteLocalAddress());

                        if (!inetAddress.isLoopbackAddress()
                                && !inetAddress.isLinkLocalAddress()
                                && inetAddress.isSiteLocalAddress()) {
                            return hostAddress;
                        }
                    }
                }
            }
            Log.d(TAG, "==============================================================");

        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Register a NetworkReceiver to be run in the main activity thread.
     *
     * @param context    The context for unregistering the NetworkReceiver.
     * @param networkReceiver   The NetworkReceiver to handle the broadcast.
     */
    public static void registerReceiver(Context context, NetworkReceiver networkReceiver) {
        context.registerReceiver(
                networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    /**
     * Unregister a previously registered NetworkReceiver.
     *
     * @param context   The context for registering a NetworkReceiver.
     * @param networkReceiver   The NetworkReceiver to unregister.
     */
    public static void unregisterReceiver(Context context, NetworkReceiver networkReceiver) {
        context.unregisterReceiver(networkReceiver);
    }
}
