package com.devmpv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application main class.
 * 
 * @author devmpv
 */
@SpringBootApplication
public class ChanReactor {

    protected ChanReactor() {
	super();
    }

    public static void main(final String[] args) {
	SpringApplication.run(ChanReactor.class, args);
    }

}
