package writing;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;

/**
 * Created by mar on 16.11.14.
 */
public class PlainFtpExport {

    private final static String computerName = System.getProperty("user.name");
    private static String hostName;

    public PlainFtpExport() {
        String server = "ftp.strato.de";
        int port = 21;
        String user = "schwittlick-computers.de";
        String pass = "pimmel";

        hostName = getLocalHostName();

        FTPClient ftpClient = new FTPClient();
        try {

            ftpClient.connect( server, port );
            ftpClient.login( user, pass );
            ftpClient.enterLocalPassiveMode();

            ftpClient.setFileType( FTP.BINARY_FILE_TYPE );
            ftpClient.mkd( "mouse" + File.separator + System.getProperty("user.name") );

            // APPROACH #1: uploads first file using an InputStream
            File firstLocalFile = new File( "/home/mar/Dropbox/workspace/MouseRecorder/saved/MOUSE000319018.csv" );

            String firstRemoteFile = "mouse" + File.separator + System.getProperty("user.name") + File.separator + "MOUSE0003190118.csv";
            InputStream inputStream = new FileInputStream( firstLocalFile );

            System.out.println( "Start uploading first file" );

            boolean done = ftpClient.storeFile( firstRemoteFile, inputStream );
            inputStream.close();
            if ( done ) {
                System.out.println( "The first file is uploaded successfully." );
            }

        } catch ( IOException ex ) {
            System.out.println( "Error: " + ex.getMessage() );
            ex.printStackTrace();
        } finally {
            try {
                if ( ftpClient.isConnected() ) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch ( IOException ex ) {
                ex.printStackTrace();
            }
        }

    }

    private String getLocalHostName() {
        try {
            return java.net.InetAddress.getLocalHost().getHostName();
        } catch ( UnknownHostException e ) {
            e.printStackTrace();
        }

        return "unknown";
    }

    public static void main( String[] args ) {
        new PlainFtpExport();
    }
}
