package it.exolab.exobank.bean;

import java.io.Serializable;
import java.util.Arrays;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
public class ScacchieraBean implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private boolean gioca;
	
	private String[][] array;

    public ScacchieraBean() {
        // Initialize your 2D array with data
        initializeArray();
    }

    private void initializeArray() {
    	
        array = new String[8][8];

        // Populate the array with data
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                array[i][j] = "Row " + i + ", Col " + j;
            }
        }
    }
    
    public String getCellValue(int i, int j) {
    	String value = array[i][j];
    	return value;
    }

    public String[][] getArray() {
        return array;
    }

    public void setArray(String[][] array) {
        this.array = array;
    }

	public boolean isGioca() {
		return gioca;
	}

	public void setGioca(boolean gioca) {
		this.gioca = gioca;
	}
	
	
	public void switchGioca() {
		gioca = !gioca;
	}

	@Override
	public String toString() {
		return "ScacchieraBean [gioca=" + gioca + ", array=" + Arrays.toString(array) + "]";
	}
	
}
