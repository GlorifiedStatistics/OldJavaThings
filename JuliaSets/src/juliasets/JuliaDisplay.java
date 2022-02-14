package juliasets;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import static juliasets.JuliaSets.say;

public class JuliaDisplay extends javax.swing.JFrame {
    public BufferedImage currImg;
    public JuliaSet js;
    public Point p1, p2;
    public double zin = 0.5, bin = 0.9;
    public boolean shift;
    public Complex center = new Complex(0,0);
    public double delta = 2;

    public JuliaDisplay(JuliaSet js) {
        this.js = js;
        initComponents();
        this.updateImage();
    }
    
    public JuliaDisplay(JuliaSet js, Complex center, double delta) {
        this.js = js;
        initComponents();
        this.center = center;
        this.delta = delta;
        this.updateImage();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        displayLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(800, 800));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                formKeyTyped(evt);
            }
        });

        mainPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mainPanelMouseClicked(evt);
            }
        });
        mainPanel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                mainPanelKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                mainPanelKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(displayLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(displayLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 357, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        if(this.currImg!=null)this.setImage(this.currImg);
    }//GEN-LAST:event_formComponentResized

    private void mainPanelKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mainPanelKeyReleased
        
    }//GEN-LAST:event_mainPanelKeyReleased

    private void mainPanelKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mainPanelKeyTyped
        
    }//GEN-LAST:event_mainPanelKeyTyped

    private void formKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyTyped
        
    }//GEN-LAST:event_formKeyTyped

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        
    }//GEN-LAST:event_formMouseClicked

    private void mainPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mainPanelMouseClicked
        say("Setting Center");
        double dx = ((double)evt.getX() - this.mainPanel.getWidth()/(double)2)/this.mainPanel.getWidth()*delta;
        double dy = ((double)evt.getY() - this.mainPanel.getHeight()/(double)2)/this.mainPanel.getHeight()*delta;
        this.center = this.center.add(new Complex(2*dx, 2*dy));
        this.updateImage();
    }//GEN-LAST:event_mainPanelMouseClicked

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        if(evt.getKeyChar() == 'p'){
            this.shift = true;
        }
        if(evt.getKeyChar() == 'o'){
            this.shift = false;
        }
        
        if(evt.getKeyChar() == 'z'){
            say("Resetting Image");
            JuliaSets.pow = 2;
            this.center = new Complex(0,0);
            this.delta = 2;
            this.updateImage();
        }
        if(evt.getKeyChar() == 'q'){
            if(this.shift){
                say("Zooming in a lot");
                this.delta *= 1-bin;
                this.updateImage();
            }else{
                say("Zooming in a little");
                this.delta *= 1-zin;
                this.updateImage();
            }
        }
        if(evt.getKeyChar() == 'e'){
            if(this.shift){
                say("Zooming out a lot");
                this.delta /= 1-bin;
                this.updateImage();
            }else{
                say("Zooming out a little");
                this.delta /= 1-zin;
                this.updateImage();
            }
        }
        if(evt.getKeyChar() == 'a'){
            double dx = this.js.xmax-this.js.xmin;
            if(this.shift){
                say("Moving left a lot");
                this.center = center.sub(new Complex(this.delta*bin, 0));
                this.updateImage();
            }else{
                say("Moving left a little");
                this.center = center.sub(new Complex(this.delta*zin, 0));
                this.updateImage();
            }
        }
        if(evt.getKeyChar() == 'd'){
            double dx = this.js.xmax-this.js.xmin;
            if(this.shift){
                say("Moving right a lot");
                this.center = center.add(new Complex(this.delta*bin, 0));
                this.updateImage();
            }else{
                say("Moving right a little");
                this.center = center.add(new Complex(this.delta*zin, 0));
                this.updateImage();
            }
        }
        if(evt.getKeyChar() == 'w'){
            double dy = this.js.ymax-this.js.ymin;
            if(this.shift){
                say("Moving up a lot");
                this.center = center.sub(new Complex(0, this.delta*bin));
                this.updateImage();
            }else{
                say("Moving up a little");
                this.center = center.sub(new Complex(0, this.delta*zin));
                this.updateImage();
            }
        }
        if(evt.getKeyChar() == 's'){
            double dy = this.js.ymax-this.js.ymin;
            if(this.shift){
                say("Moving down a lot");
                this.center = center.add(new Complex(0, this.delta*bin));
                this.updateImage();
            }else{
                say("Moving down a little");
                this.center = center.add(new Complex(0, this.delta*zin));
                this.updateImage();
            }
        }
        if(evt.getKeyChar() == 'n'){
            JuliaSets.pow -= 0.1;
            this.updateImage();
        }
        if(evt.getKeyChar() == 'm'){
            JuliaSets.pow += 0.1;
            this.updateImage();
        }
    }//GEN-LAST:event_formKeyPressed

    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased
       
    }//GEN-LAST:event_formKeyReleased

    
    public void updateImage(){
        this.js.setBounds(this.center.r-this.delta, this.center.r+this.delta,
                this.center.c-this.delta, this.center.c+this.delta);
        this.setImage(this.js.buildImage());
        say("Center: " + this.center + "  Delta: " + this.delta);
        say("new Complex("+this.center.r+", "+this.center.c+"), "+this.delta);
    }

    public void setImage(BufferedImage img) {
        currImg = img;
        int size = Math.max(this.getWidth(), this.getHeight());
        img = Utils.resize(img, size, size);
        this.displayLabel.setIcon(new ImageIcon(img));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel displayLabel;
    private javax.swing.JPanel mainPanel;
    // End of variables declaration//GEN-END:variables
}
