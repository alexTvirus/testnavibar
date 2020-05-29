/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CustomException;

/**
 *
 * @author Alex
 */
public class WrongProxyException extends Exception {

    private String message;

    public WrongProxyException(String ms) {
        this.message = ms;
    }

    public WrongProxyException() {
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message2) {
        this.message = message2;
    }
}
