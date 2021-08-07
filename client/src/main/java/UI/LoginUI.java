package UI;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;

import Engine.GameGlobals;
import Engine.Network;
import UI.Components.Button;
import UI.Components.TextBox;

public class LoginUI extends UI {
    private boolean started = false;
    private boolean ended = false;

    private TextBox txtIP;
    private Button btnLogin;

    public LoginUI() {
        this.txtIP = new TextBox("127.0.0.1", 0, 0);
        this.txtIP.setAbsoluteCoords(true);
        this.txtIP.setCenterScreen(true);

        this.btnLogin = new Button("Entrar", 0, 0);
        this.btnLogin.setActiveColor(Color.GREEN);
        this.btnLogin.setInactiveColor(Color.DARK_GRAY);
        this.btnLogin.setAbsoluteCoords(true);
        this.btnLogin.setCenterScreen(true);
        this.btnLogin.setScreenY(this.btnLogin.getScreenY() + 100);
        this.btnLogin.setHandler(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (txtIP.getText().trim().equals("")) {
                    JOptionPane.showMessageDialog(null, "Digite o IP", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                txtIP.setListening(false);
                GameGlobals.network = new Network(txtIP.getText().trim(), 8080);
                ended = true;
            }
        });
    }

    public boolean isEnded() {
        return this.ended;
    }

    @Override
    public void draw(Graphics2D g) {
        if (this.ended) {
            return;
        }

        if (!this.started) {
            //
        }

        this.started = true;

        this.txtIP.draw(g);
        this.btnLogin.draw(g);
    }
}
