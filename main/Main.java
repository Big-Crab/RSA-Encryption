package main;

//TODO Add key saving thing, add key file encrypter with Password with 3 attempts per hour.
//Compile
//Make/add web-app!
//Add send-self the private data
//Contacts as TXT file in program's install dir
//USE XML! - DOM parser?


import java.math.BigInteger;
import java.util.Arrays;
import java.util.Properties;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.*;
import java.awt.event.*;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;

public class Main implements ActionListener{
	AQAConsole2015 console = new AQAConsole2015();
	BigInteger pGlobal = BigInteger.ZERO;
	BigInteger qGlobal = BigInteger.ZERO;
	BigInteger phiGlobal = BigInteger.ZERO;
	BigInteger eGlobal = BigInteger.ZERO;
	BigInteger dGlobal = BigInteger.ZERO;
	BigInteger nGlobal /*= BigInteger.ZERO*/;
	boolean sendPrivateData = false;
	boolean manualMode = false;
	String[] keysStore;

	public static void main(String[] args){
		new Main();
	}
	public Main(){

		String fileData[] = new String[5];
		DataAcquirer da = new DataAcquirer();
		da.giveKeys(0, fileData);

		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Clipboard clipboard = toolkit.getSystemClipboard();

		JFrame frame = new JFrame();
		
		//button.setBorderPainted(false);
		//button.setFocusPainted(false);
		//button.setContentAreaFilled(false);
		
		JMinimalistButton buttonEncrypt = new JMinimalistButton();
		JMinimalistButton buttonDecrypt = new JMinimalistButton();
		JMinimalistButton buttonCopy = new JMinimalistButton();

		JMinimalistButton buttonSave = new JMinimalistButton();
		JMinimalistButton buttonLoad = new JMinimalistButton();

		JMinimalistLabel labelPrimeP = new JMinimalistLabel("Prime P: Not Generated");
		JMinimalistLabel labelPrimeQ = new JMinimalistLabel("Prime Q: Not Generated");
		JMinimalistLabel labelPrimeN = new JMinimalistLabel("Prime Product N: Not Generated");
		JMinimalistLabel labelPrimePhi = new JMinimalistLabel("Phi: Not Generated");
		JMinimalistLabel labelPubKey = new JMinimalistLabel("Public Key: Not Generated");
		JMinimalistLabel labelPrivKey = new JMinimalistLabel("Private Key: Not Generated");
		JMinimalistButton buttonCustom = new JMinimalistButton();
		
		JMinimalistCheckbox developerBox = new JMinimalistCheckbox();
		JMinimalistCheckbox boxManual = new JMinimalistCheckbox();

		JMinimalistLabel labelMailTo = new JMinimalistLabel("Send To: ");
		JMinimalistLabel labelMailFrom = new JMinimalistLabel("Sent From: ");
		JMinimalistLabel labelMailFromPwd = new JMinimalistLabel("Password:");
		JMinimalistField fieldMailTo = new JMinimalistField();
		JMinimalistField fieldMailFrom = new JMinimalistField();
		JMinimalistPasswordField fieldMailFromPwd = new JMinimalistPasswordField();
		JMinimalistButton buttonSend = new JMinimalistButton();
		JMinimalistButton buttonSendAuto = new JMinimalistButton();

		JMinimalistLabel labelInput = new JMinimalistLabel("Input:");
		JMinimalistLabel labelResult = new JMinimalistLabel("Result:");
		JMinimalistLabel labelOutput = new JMinimalistLabel("");
		
		JMinimalistLabel[] labelsList = {labelPrimeP, labelPrimeQ, labelPrimeN, labelPrimePhi, labelPubKey, labelPrivKey};

		labelOutput.setOpaque(true);
		labelOutput.setBackground(Color.white);

		JMinimalistField fieldInput = new JMinimalistField();

		Insets insets = frame.getInsets();

		buttonEncrypt.setText("Encrypt");
		buttonEncrypt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//labelOutput.setText(Encrypt(fieldInput.getText(), fieldKey.getText()));
				labelOutput.setText(CipherAsymmetrical(true, fieldInput.getText(), (int) Math.pow(2, 12)));
				labelPrimeP.setText("Prime P: " + pGlobal);
				labelPrimeQ.setText("Prime Q: " + qGlobal);
				labelPrimeN.setText("Prime Product N: " + ((pGlobal.multiply(qGlobal) == BigInteger.ZERO) ? nGlobal : pGlobal.multiply(qGlobal)));
				labelPrimePhi.setText("Prime Phi: " + phiGlobal);
				labelPubKey.setText("Public Key: " + eGlobal);
				labelPrivKey.setText("Private Key: " + dGlobal);
				buttonSend.setEnabled(true);
				buttonSendAuto.setEnabled(true);
			}
		});
		buttonDecrypt.setText("Decrypt");
		buttonDecrypt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//labelOutput.setText(Decrypt(fieldInput.getText(), fieldKey.getText()));
				labelOutput.setText(CipherAsymmetrical(false, fieldInput.getText(), (int) Math.pow(2, 12)));
			}
		});

		buttonCopy.setText("Clipboard");
		buttonCopy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StringSelection strSel = new StringSelection(labelOutput.getText());
				clipboard.setContents(strSel, null);
			}
		});
		buttonCustom.setText("Manual");
		buttonCustom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JMinimalistField fieldCryptE = new JMinimalistField();
				JMinimalistField fieldCryptD = new JMinimalistField();
				JMinimalistField fieldCryptP = new JMinimalistField();
				JMinimalistField fieldCryptQ = new JMinimalistField();
				JMinimalistField fieldCryptN = new JMinimalistField();
				Object[] notification = {
						"E Value:", 	fieldCryptE,
						"D Value:", 	fieldCryptD,
						"P Value:", 	fieldCryptP,
						"Q Value:", 	fieldCryptQ,
						"N Value:", 	fieldCryptN
				};		
				int option = JOptionPane.showConfirmDialog(null, notification, "Decrypt Values", JOptionPane.OK_CANCEL_OPTION);
				if(option == JOptionPane.OK_OPTION){
					boxManual.setSelected(true);
					manualMode = true;
					
					eGlobal = (fieldCryptE.getText().hashCode() == 0) ? eGlobal : BigInteger.valueOf(Integer.parseInt(fieldCryptE.getText()));
					dGlobal = (fieldCryptD.getText().hashCode() == 0) ? dGlobal : BigInteger.valueOf(Integer.parseInt(fieldCryptD.getText()));
					pGlobal = (fieldCryptP.getText().hashCode() == 0) ? pGlobal : BigInteger.valueOf(Integer.parseInt(fieldCryptP.getText()));
					qGlobal = (fieldCryptQ.getText().hashCode() == 0) ? qGlobal : BigInteger.valueOf(Integer.parseInt(fieldCryptQ.getText()));
					nGlobal = (fieldCryptN.getText().hashCode() == 0) ? nGlobal : BigInteger.valueOf(Integer.parseInt(fieldCryptN.getText()));

					labelPrimeP.setText("Prime P: " + pGlobal);
					labelPrimeQ.setText("Prime Q: " + qGlobal);
					labelPrimeN.setText("Prime Product N: " + ((pGlobal.multiply(qGlobal) == BigInteger.ZERO) ? nGlobal : pGlobal.multiply(qGlobal)));
					if(nGlobal != null){
						labelPrimeN.setText("Prime Product N: " + nGlobal);
					}
					labelPubKey.setText("Public Key: " + eGlobal);
					labelPrivKey.setText("Private Key: " + dGlobal);
				}
			}
		});

		developerBox.setText("Include Private Key?");
		developerBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendPrivateData = developerBox.isSelected();
			}
		});
		boxManual.setText("Use Manual Values?");
		boxManual.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				manualMode = boxManual.isSelected();
			}
		});

		buttonSend.setText("Send EMail");
		buttonSendAuto.setText("Send Public Key");
		buttonSend.setEnabled(false);
		buttonSendAuto.setEnabled(false);
		buttonSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SendMail((sendPrivateData == true) ? 'p':'m', fieldMailTo.getText(), fieldMailFrom.getText(), fieldMailFromPwd.getText(), labelOutput.getText());
			}
		});
		buttonSendAuto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SendMail('a', fieldMailTo.getText(), fieldMailFrom.getText(), fieldMailFromPwd.getText(), labelOutput.getText());
			}
		});

		buttonSave.setText("Save Key");
		buttonLoad.setText("Load Key");
		keysStore = new String[5];
		buttonLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				keysStore = da.giveKeys(0, keysStore);
				eGlobal = BigInteger.valueOf(Integer.parseInt(keysStore[1]));
				dGlobal = BigInteger.valueOf(Integer.parseInt(keysStore[2]));
				pGlobal = BigInteger.valueOf(Integer.parseInt(keysStore[3]));
				qGlobal = BigInteger.valueOf(Integer.parseInt(keysStore[4]));
				nGlobal = ((pGlobal.multiply(qGlobal) == BigInteger.ZERO) ? nGlobal : pGlobal.multiply(qGlobal));
				
				RefreshLabels(labelsList);
			}
		});
		
		buttonSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("eGlob: " + eGlobal.toString());
				keysStore[1] = eGlobal.toString();
				keysStore[2] = dGlobal.toString();
				keysStore[3] = pGlobal.toString();
				keysStore[4] = qGlobal.toString();
				da.saveKeys(0, keysStore);
			}
		});

		frame.setLayout(null);
		frame.setSize(new Dimension(500,800));
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		frame.getContentPane().add(buttonEncrypt);
		frame.getContentPane().add(buttonDecrypt);
		frame.getContentPane().add(buttonCopy);

		frame.getContentPane().add(labelInput);
		frame.getContentPane().add(labelResult);
		frame.getContentPane().add(labelPrimeP);
		frame.getContentPane().add(labelPrimeQ);
		frame.getContentPane().add(labelPrimeN);
		frame.getContentPane().add(labelPrimePhi);
		frame.getContentPane().add(labelPubKey);
		frame.getContentPane().add(labelPrivKey);
		frame.getContentPane().add(buttonCustom);

		frame.getContentPane().add(labelMailTo);
		frame.getContentPane().add(labelMailFrom);
		frame.getContentPane().add(labelMailFromPwd);
		frame.getContentPane().add(fieldMailTo);
		frame.getContentPane().add(fieldMailFrom);
		frame.getContentPane().add(fieldMailFromPwd);
		frame.getContentPane().add(buttonSend);
		frame.getContentPane().add(buttonSendAuto);

		frame.getContentPane().add(fieldInput);
		frame.getContentPane().add(labelOutput);

		frame.getContentPane().add(developerBox);
		frame.getContentPane().add(boxManual);
		frame.getContentPane().add(buttonSave);
		frame.getContentPane().add(buttonLoad);
		
		///////////Keep it plain/////////
		
		/////////////////////////////////

		buttonEncrypt.setBounds(insets.left + 110, 90 + insets.top, 120, 30);
		buttonDecrypt.setBounds(insets.left + 250, 90 + insets.top, 120, 30);

		labelInput.setBounds(insets.left + 210, insets.top + 0, 60, 40);
		fieldInput.setBounds(insets.left + 110, insets.top + 30, 260, 40);

		labelResult.setBounds(insets.left + 210, insets.top + 130, 60, 20);
		labelOutput.setBounds(insets.left + 110, insets.top + 150, 260, 50);
		buttonCopy.setBounds(insets.left + 240, insets.top + 220, 130, 50);
		buttonCustom.setBounds(insets.left + 110, insets.top + 220, 130, 50);

		labelPrimeP.setBounds(insets.left + 110, insets.top + 270, 260, 50);
		labelPrimeQ.setBounds(insets.left + 110, insets.top + 290, 260, 50);
		labelPrimePhi.setBounds(insets.left + 110, insets.top + 310, 260, 50);

		labelPrimeN.setBounds(insets.left + 110, insets.top + 360, 260, 50);
		labelPubKey.setBounds(insets.left + 110, insets.top + 380, 260, 50);
		labelPrivKey.setBounds(insets.left + 110, insets.top + 400, 260, 50);

		labelMailTo.setBounds(insets.left + 110, insets.top + 450, 260, 50);
		fieldMailTo.setBounds(insets.left + 110, insets.top + 485, 260, 30);

		labelMailFrom.setBounds(insets.left + 110, insets.top + 510, 260, 50);
		fieldMailFrom.setBounds(insets.left + 110, insets.top + 545, 260, 30);

		labelMailFromPwd.setBounds(insets.left + 110, insets.top + 570, 260, 50);
		fieldMailFromPwd.setBounds(insets.left + 110, insets.top + 605, 260, 30);

		buttonSend.setBounds(insets.left + 110, insets.top + 645, 130, 30);
		buttonSendAuto.setBounds(insets.left + 240, insets.top + 645, 130, 30);
		developerBox.setBounds(insets.left + 170, insets.top + 675, 260, 30);
		boxManual.setBounds(insets.left + 170, insets.top + 700, 260, 30);

		buttonSave.setBounds(insets.left + 10, insets.top + 30, 90, 40);
		buttonLoad.setBounds(insets.left + 10, insets.top + 80, 90, 40);		
	}
	
	private void RefreshLabels(JMinimalistLabel[] label){
		label[0].setText("Prime P: " + pGlobal);
		label[1].setText("Prime Q: " + qGlobal);
		label[2].setText("Prime N: " + ((pGlobal.multiply(qGlobal) == BigInteger.ZERO) ? nGlobal : pGlobal.multiply(qGlobal)));
		label[3].setText("Prime Phi: " + phiGlobal);
		label[4].setText("Prime E: " + eGlobal);
		label[5].setText("Prime D: " + dGlobal);
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
	}

	private void SendMail(char type, String fieldMailTo, String fieldMailFrom, String fieldMailFromPwd, String labelOutput){
		if(fieldMailTo == null || fieldMailFrom == null || fieldMailFromPwd == null){
			System.out.println("No.");
		}
		else{
			String sender = fieldMailFrom;
			String password = fieldMailFromPwd;
			String recipient = fieldMailTo;
			String messageText = "N/A";
			if(type == 'm'){
				messageText = new String("Public Encryption Key (E): " + eGlobal + "\nPrime Product Value (N): " + ((pGlobal.multiply(qGlobal) == BigInteger.ZERO) ? nGlobal : pGlobal.multiply(qGlobal)) + "\n\nMessage: \n" + labelOutput + "\n\nOpen the AsymGen Program and select \"Manual\" from the options, input these values and encrypt a message.\nTo reply to this email, either reply with the numerical-dashed code generated, or use the in-program sender (Update your settings, if GMail, to allow the program to send the email: www.google.com/settings/security/lesssecureapps)");
			}
			else if(type == 'p'){
				messageText = new String("Public Key (E): " + eGlobal + "\nFirst Prime Value (P): " + pGlobal + "\nSecond Prime Value (Q): " + qGlobal + "\n\nMessage: \n" + labelOutput + "\n\nOpen the AsymGen Program and select \"Manual\" from the options, input these values and decrypt the numerical values (Including dashes)");				
			}
			else if(type == 'a'){
				messageText = new String("Their Public Encryption Key (E): " + eGlobal + "\nTheir Prime Product Value (N): " + ((pGlobal.multiply(qGlobal) == BigInteger.ZERO) ? nGlobal : pGlobal.multiply(qGlobal)) + "\n\nOpen the AsymGen Program and select \"Manual\" from the options, input these values and encrypt a message with it.\nTo reply, either use your emailing preference to reply with the numerical-dashed code generated, or use the in-program sender (Update your settings, if GMail, to allow the program to send the email: www.google.com/settings/security/lesssecureapps)");
			}



			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.user", sender);
			props.put("mail.smtp.password", password);
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.port", "587");

			Session session = Session.getInstance(props,
					new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(sender, password);
				}
			});

			try {
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(sender));
				message.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse(recipient));

				//Get the first part of the email as the sender's name
				int atSymbChar = sender.length();
				for(int i = 0; i < sender.length(); i++){
					if(sender.charAt(i) == '@'){
						atSymbChar = i;
					}
				}
				if(type == 'p'){
					message.setSubject("AssymGen Encrypted + Private Data from: " + sender.substring(0, atSymbChar));
				}
				else if (type == 'a'){
					message.setSubject("AssymGen Public Key from: " + sender.substring(0, atSymbChar));
				}
				else if (type == 'm'){
					message.setSubject("AssymGen Encrypted Data from: " + sender.substring(0, atSymbChar));
				}
				message.setText(messageText);
				Transport.send(message);
				System.out.println("Done");

			} catch (MessagingException exc) {
				throw new RuntimeException(exc);
			}
		}

	}

	private String CipherAsymmetrical(boolean encrypt, String sentence, int upperBound){
		RandomPrimeGenerator ranPrime = new RandomPrimeGenerator();

		BigInteger n = BigInteger.ZERO;
		BigInteger phi = BigInteger.ZERO;
		BigInteger e = BigInteger.valueOf(3);
		BigInteger d = BigInteger.ZERO;
		BigInteger p = BigInteger.ZERO;
		BigInteger q = BigInteger.ZERO;
		int length;

		String decResult;

		BigInteger bigChar = BigInteger.ZERO;
		StringBuilder strBuilder = new StringBuilder();
		String finalString = null;


		//CALC KEYS
		if(encrypt){
			String charPairs[];
			if(sentence.length() % 2 != 0){
				sentence = sentence + " ";
			}
			length = sentence.length();
			charPairs = new String[(int) (length / 2)];

			if(!manualMode){
				p = BigInteger.valueOf(ranPrime.givePPrime(upperBound, upperBound/10));
				q = BigInteger.valueOf(ranPrime.giveQPrime(upperBound, upperBound/10));
				System.out.println("P: " + p);			
				System.out.println("Q: " + q);
				n = p.multiply(q);
				phi = p.subtract(BigInteger.valueOf(1)).multiply(q.subtract(BigInteger.valueOf(1)));

				while(!ranPrime.isRelativelyPrime(e, phi)){
					e = e.add(BigInteger.valueOf(1));
				}
				System.out.println("E: " + e);
				d = ranPrime.GetD(e, phi);
				System.out.println("D:" + d);
				
			}
			else{
				// ???? phi = phiGlobal;
				d = dGlobal;
				e = eGlobal;
				p = pGlobal;
				q = qGlobal;
				n = nGlobal;
				phi = (pGlobal.subtract(BigInteger.ONE)).multiply(qGlobal.subtract(BigInteger.ONE));
			}



			phiGlobal = phi; dGlobal = d; eGlobal = e; pGlobal = p; qGlobal = q;
			//GET CHAR PAIRS
			int j=0;
			for(int i=0;i<charPairs.length;i++){
				charPairs[i] = String.valueOf(sentence.charAt(j)) + String.valueOf(sentence.charAt(j+1));
				j+=2;
			}


			for(int i=0;i<charPairs.length;i+=1){
				bigChar = new BigInteger(charPairs[i].getBytes());
				System.out.println("getBytes at " + i +": " + bigChar);
				bigChar = bigChar.modPow(e, n);
				System.out.println("EN bigChar pair " + i + ": " + bigChar);
				finalString = bigChar.toString();

				if(i<charPairs.length-1){
					strBuilder.append(finalString + "-");
				}
				else{
					strBuilder.append(finalString);
				}

			}
			finalString = strBuilder.toString();
		}
		else{ //USE KEYS FOR DEC
			n = ((pGlobal.multiply(qGlobal) == BigInteger.ZERO) ? nGlobal : pGlobal.multiply(qGlobal));
			d = dGlobal;
			e = eGlobal;

			int startId = 0, j=0, numOfChars=1;
			String charPairsDec[];
			for(int i=0; i < sentence.length(); i++){
				if(sentence.charAt(i) == '-'){
					numOfChars++;
				}
			}
			charPairsDec = new String[numOfChars];



			for(int i=0; i < sentence.length();i++){
				if(sentence.charAt(i) == '-'){
					charPairsDec[j] = sentence.substring(startId, i);
					j++;
					startId = i + 1;
				}
				else if(i == sentence.length()-1){
					charPairsDec[j] = sentence.substring(startId, sentence.length());
				}
			}
			if(numOfChars == 1){
				charPairsDec[0] = sentence;
			}
			for(int i=0; i < numOfChars; i++){
				bigChar = new BigInteger(charPairsDec[i]);
				bigChar = bigChar.modPow(d, n);
				finalString = ranPrime.getString(bigChar.toByteArray());
				System.out.println("DE decResult at " + i + ": " + finalString);
				strBuilder.append(finalString + "-");
			}
			finalString = strBuilder.toString();

			//Reset, recycle!
			numOfChars = 0;
			startId = 0;
			j = 0;

			for(int i=0; i < finalString.length(); i++){
				if(finalString.charAt(i) == '-'){
					numOfChars++;
				}
			}
			char[] convertedChars = new char[numOfChars];

			for(int i=0; i < finalString.length(); i++){
				if(finalString.charAt(i) == '-'){
					convertedChars[j] = (char) (Integer.parseInt(finalString.substring(startId, i)));
					j++;
					startId = i + 1;
				}
			}
			finalString = String.valueOf(convertedChars);
		}
		return (finalString);
		//charArray[i] = (char) wrappedChar;
	}
}