import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;

import javax.swing.JFrame;

import Engine.GameGlobals;
import Engine.Settings;

/**
 *          /:::
            ;  ':/. _
            `     -√
            .       :-
            |        :'
           ,,         ;                     __',,,,,"-------------"
           -          -                ',,'-,,'''                '',---+--''__-__          '--//                 '--//
          ,'          -            ".-+-''                    `__''     '''  _'.'        `__`  :'              '__`  :`
           .          -       -'-·:.·               ...---::--.+++,---,+-- ''     --.   _:`   :'              .:'   :'
         `-           ;,     ___--            `__-/.--------.:        '/...:',_,   .:--`--   `.              :.    '.
         ;             _ ,_,`             `.:/:::.''''       --      '-::--.       -/::-`    :              __     :
         '.            '/:'            _-+-:.''         ·     _:- `·-·'      '---'  /-::::   /              /      /
         `.             '_,'        `.::-`             ·-       ' ''            ''-.:. `-::` /             :'      /
         _                ·-;.'   ,::.'               __        ·              '        '-:-:·           _-       :`
         |-                  ·,-+-,                  '-        '-              :.         '_/.          _-        _-
         '/       ,           ·_·                    :         ·                /           ':-       '-.          /
          :      ;  ,        __                     -'         :                --            ./.   `,.`           /
          .-      '' '     ·.·                     '-''''     ':                 .             ./-.-.·             /
           `:`      ,,-'. ·-              __   ''../...'''____-.                 ''              +·                /
            ':-       '·''·               /   __' .:         '/                  ·_              :'/              -.
             /.;;'       -               '-       :           /                ''_/''            -.':            '/
            ;;-·'·--''  ,               :        __          .:               _''':''''___`       : :' '' _''   '/
           '√'      ·--+♪              ':       `:           /.                   :       _-_     :  / -.-...  -:
          ';            ♪              :'       ::          .:               -    ::              :' :' :.`  ,:.
          :            ';              /       .-:          .:    -          -    :/              __  / ` _-:.
         --            :`             ':      ':.`         -.:    /          `    ::`         /   `:  :---`..
        ';             ';             /`      -./          : :    :          ''   /.:        -`    /  `:    -
       '♪            ,;'              /       : /         .: :   `:          ''   /':        '     /   :    ''
''   .--          _.'-                /      :-'/         /'':   -;               / :              :   -'   '-
 ♪·--'           '_;  _              .-      /`-:     '   :'':   //          _`   :'-        -`    :    _   -.
 ,·'           '..'. '.              .'     -.':.    .:  --'-:   /\          :-   / _'       ''    :    :   :
   ._..'            ·,·              ;      /'':',,,,:/''/.'/-   /\`         ::   /  _       _     /    _  .-
     '''·-···-·----- ♪               ;     ··`.:.'''-:-.-:-:--   /\`         /-   / '_      -:     /    _  /
         :  '''''   :/-             '-::___:.-::/-/-::-.:-.:.-   //`       '.:/___:'':      ::    _-    _` /
        :'          //-.            .':/-__/-'../..':/'':''::-  `::`       '.:/''_:-':      :/    :`    __ .:`
       .:          ::/-:            - -::-.-/'''---+-+::/--+--' '-.'      :-//:-'-:-,:     :`:    /     __  '---
       /'         ':::-/            --//....   -:      /  .' '.--· _--++·`/./`:-'-/--/..''':':    /     .:'''.-/
      :.          :::-./            .` :'-...  -:  '_.'/  .          ''.'.h:-.u/::-''/''+++::·   `:     .:....·
     '-          -/.'.'/            -  __  '/''''  :_.': ''              ';----';''''-.:-.-::·   :'     _·
    '·   ;      ·/:'/.·/            '  .'   -://::-' ':'''               :'__' _:   '  ': :.`:··:/:  :  :`
   ';   .      '/:- -  /           ''   ___.-  ''-.'-:'''                :-.:. -:  -::''/ - .:--:'   /  /
  _<    .      :-·'   :·           ·      ·_,,,,,:-:-''                  :` .:''''./ ./:..` /-''/    /  /
 .,    ·      `" .-   ·/           ·                                     ':'--:::/:  '+-.`  .-.'':   :::-
'·/    /      /·  .'  '_           ·   .·. .                              '-4 ''''/.:_,,'   : ''--  `;/ `
:^:^   ;/;.   "'   |'  ':          -   ·____;                                ...--:--'     _· · -   -/
·:·;.  .2'-- '^    ·__··/     ·    :     .,,..                                '            _ '' :   ::
  __·   ;♪ --.'     ''..:                                      ^                 ,,,       :` ''':   ♪
   ·__· ·,_'//      '' '--    -    -                                          _-   -      /  '' /  ·
      _,_';/':,  '  ''' _/                                                     ... .-     :.''  /  ..
         _:;;:/' '  '   ':.   :    ''                                                     :''  '/  :
            .:.:''--_____'/         :                                                    ./ '  -'  :
                '____...../.  .     ..:.                   -+-+`                        -: ''''-  ♪
                  __.      /        :  `--.              -/`  `/                     `-._  '''__  :
                    '..."  _.   ·   .'    '...'         ,-    `/                 `--;-'  _  _♪   `:
                         .../   :   ./.        ___'_    :`   ./`            `-.--.''     `/'-/   ♪
                        ·...:'  :.   /'''''''.',,,/:;..:'''''''''''_'..-/`---'''''       -:''"   :
                            ':  :.   :-----"                              --::---------:-/`'':  _
                             /  :.'  -''''                                    ''''''''   /`'.:  :
                             ·- :-"  `-                                                 `- '   .-
                              :':.:.  "                                                 :''    :
                              '.:  -.'"                                                 /''.- .-
                                    ···                                                 --.♪  /
                                                                                          .:  ♪
                                                                                           :__
                                                                                            ·u·
 */

public class Main extends JFrame {
    Desenho des = new Desenho();

    public String globMove;

    Main() {
        super("Trabalho");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.getContentPane().setLayout(new GridBagLayout());
        this.getContentPane().setBackground(Color.BLACK);
        this.getContentPane().add(des);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                globMove = KeyEvent.getKeyText(e.getKeyCode());
                if (e.getKeyCode() == KeyEvent.VK_F11 || (e.getKeyCode() == KeyEvent.VK_ENTER && e.isAltDown())) {
                    Settings.toggleFullScreen();

                    return;
                } else if (e.getKeyCode() == Settings.KEY_PAUSE) {
                    GameGlobals.paused = !GameGlobals.paused;
                } else if (e.getKeyCode() == KeyEvent.VK_F3) {
                    Settings.debugInfo = !Settings.debugInfo;
                }

                Iterator<KeyListener> l = GameGlobals.keyListeners.iterator();
                while (l.hasNext()) {
                    try {
                        KeyListener listener = l.next();
                        listener.keyPressed(e);
                    } catch (Throwable ex) {
                        ex.printStackTrace();
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                Iterator<KeyListener> l = GameGlobals.keyListeners.iterator();
                while (l.hasNext()) {
                    try {
                        KeyListener listener = l.next();
                        listener.keyReleased(e);
                    } catch (Throwable ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    static public void main(String[] args) {
        Main f = new Main();
        f.setVisible(true);

        GameGlobals.mainWindow = f;
    }
}
