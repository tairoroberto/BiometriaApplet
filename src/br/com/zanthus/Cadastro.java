package br.com.zanthus;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import br.com.zanthus.util.ButtonLayout;
import veridis.biometric.BiometricImage;
import veridis.biometric.BiometricTemplate;


public class Cadastro {
	
	JTextField textField;
	JButton btnOK, btnCancel;
	
	Cadastro(){
		/*creates buttons*/
		btnOK = new JButton("OK");
		btnCancel = new JButton("Cancelar");
		/*creates textField to recieve the identifier*/
		textField = new JTextField("", 15);
		/*creates panel to organize buttons*/
		JPanel buttonsPane = new JPanel();
		buttonsPane.setLayout(new BoxLayout(buttonsPane, BoxLayout.X_AXIS));
			
		/*creates main frame*/
		final JFrame frame = new JFrame("Veridis - Cadastro");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		/*gets container where components will be organized*/
		Container contentPane = frame.getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		
		/*add buttons to pane*/
		buttonsPane.add(btnOK);
		buttonsPane.add(btnCancel);
		/*configures border of textfield*/
		textField.setBorder(new TitledBorder(new EtchedBorder(),
				"Identificador:"));	
		ButtonLayout.changeBoth(textField);
		/*adds text field*/
		contentPane.add(textField);
		contentPane.add(buttonsPane);
		
		frame.pack();
		frame.setVisible(true);

		
		/* Cancel event: closes the window */
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
			}
		});
		/* Finishes register and erases data */
		btnOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				BiometricImage image = Main.getImage();
				
				if(image != null){
					String input = textField.getText().toString();
					/* adds to database */
					try{
						JOptionPane.showMessageDialog(null, "Realizando o cadastro.");
						try{
							new BiometricTemplate(image);
						}catch(Exception e){
							System.out.println(e.getMessage());
							
						}
						if (dbAccess.AddTemplate(new BiometricTemplate(image), input)) {
							JOptionPane.showMessageDialog(null, "Usuario " + input
									+ " cadastrado");
						} else {
							JOptionPane.showMessageDialog(null, "Usuario " + input
									+ " não pode ser cadastrado");
						}
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "Usuario " + input
								+ " não pode ser cadastrado");
					}
				}
				else JOptionPane.showMessageDialog(null, "Nenhuma imagem está disponível.");
			}
		});
	}
	public static void main(String args[]) {
		new Cadastro();
	}
}
