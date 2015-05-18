package pdstore.ui.treeview;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import pdstore.Blob;
import pdstore.GUID;
import pdstore.PDStore;

public class PDTreeRenderer extends DefaultTreeCellRenderer {
	private static final long serialVersionUID = 1L;
	
	private Icon instanceIcon, linkIcon, usedIcon, primitiveIcon, leafIcon, recurseIcon, homeIcon;
	PDStore store;
	
    public PDTreeRenderer(PDStore store) {
    	this.store = store;
    	instanceIcon = new ImageIcon("icons/nuvola_selected/ledblue.png");
        linkIcon = new ImageIcon("icons/nuvola_selected/folder_green.png");
        usedIcon = new ImageIcon("icons/nuvola_selected/folder_yellow.png");
        recurseIcon = new ImageIcon("icons/nuvola_selected/forward.png");
        primitiveIcon = new ImageIcon("icons/nuvola_selected/kdf.png");
        leafIcon = new ImageIcon("icons/nuvola_selected/ledgreen.png");
        homeIcon = new ImageIcon("icons/nuvola_selected/folder_home.png");
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel,
                        boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        
        if (value instanceof InstanceNode) {
        	InstanceNode node = (InstanceNode)value;
        	
        	GUID transaction = store.begin();
        	Blob icon = store.getIcon(transaction, node.getValue());
        	store.commit(transaction);
        	if(icon!=null){
        		setIcon(!node.isCyclical() ? (reSize(icon.getData())) : (reSize(icon.getData())));
        		setToolTipText(String.valueOf(node.getValue()));
        	}else{
        		setIcon(!node.isCyclical() ? recurseIcon : instanceIcon);
        		setToolTipText(String.valueOf(node.getValue()));
        	}
        }
        else if (value instanceof PrimitiveRoleNode) {
        	GUID transaction = store.begin();
        	Blob icon = store.getIcon(transaction, ((PrimitiveRoleNode)value).getRole());
        	store.commit(transaction);
        	if(icon!=null){
        		setIcon(reSize(icon.getData()));
        	}else{
        		setIcon(primitiveIcon);
        	}
        }
        else if (value instanceof ComplexRoleNode) {
        	ComplexRoleNode node = (ComplexRoleNode)value;
        	GUID transaction = store.begin();
        	Blob icon = store.getIcon(transaction, node.getRole());
        	store.commit(transaction);
        	if(icon!=null){
        		if(node.isTypeRole)
        			setIcon(reSize(icon.getData()));
        		else 
        			setIcon(reSize(icon.getData()));
        	}else{
        		if(node.isTypeRole)
        			setIcon(linkIcon);
        		else 
        			setIcon(usedIcon);
        	}
        	setToolTipText(String.valueOf(node.getRole()));
        }
        else if (value instanceof PDRootNode) {
        	setIcon(homeIcon);
        }
        else if (leaf) {
        	setIcon(leafIcon);
        }
        
        return this;
    }
    
    /**
     * This method resizes the icon to
     * an appropriate size for the tree
     * nodes so that the sizes are consistent
     * throughout
     */
    public ImageIcon reSize(byte[] icon){
    	ImageIcon newIcon = new ImageIcon(icon);
    	Image img = newIcon.getImage();
    	BufferedImage bi = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
    	Graphics g = bi.createGraphics();
    	g.drawImage(img, 0, 0, 16, 16, null);
    	newIcon = new ImageIcon(bi);
    	
    	return newIcon;
    }
}