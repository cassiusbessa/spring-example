package com.springboot.example.restful;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MathController {
    
    
        @GetMapping("/sum/{num1}/{num2}")
        public Double sum(@PathVariable(value = "num1") String num1, @PathVariable(value = "num2") String num2) {
            
            if (!isNumeric(num1) || !isNumeric(num2)) {
                throw new UnsupportedOperationException("Please set a numeric value!");
            }
            return convertToDouble(num1) + convertToDouble(num2);
        }

        @GetMapping("/sub/{num1}/{num2}")
        public Double sub(@PathVariable(value = "num1") String num1, @PathVariable(value = "num2") String num2) {
            
            if (!isNumeric(num1) || !isNumeric(num2)) {
                throw new UnsupportedOperationException("Please set a numeric value!");
            }
            return convertToDouble(num1) - convertToDouble(num2);
        }

        @GetMapping("/mult/{num1}/{num2}")
        public Double mult(@PathVariable(value = "num1") String num1, @PathVariable(value = "num2") String num2) {
            
            if (!isNumeric(num1) || !isNumeric(num2)) {
                throw new UnsupportedOperationException("Please set a numeric value!");
            }
            return convertToDouble(num1) * convertToDouble(num2);
        }

        @GetMapping("/div/{num1}/{num2}")
        public Double div(@PathVariable(value = "num1") String num1, @PathVariable(value = "num2") String num2) {
            
            if (!isNumeric(num1) || !isNumeric(num2)) {
                throw new UnsupportedOperationException("Please set a numeric value!");
            }
            if (convertToDouble(num2) == 0) {
                throw new UnsupportedOperationException("Division by zero is not allowed!");
            }
            return convertToDouble(num1) / convertToDouble(num2);
        }

        @GetMapping("/med/{num1}/{num2}")
        public Double med(@PathVariable(value = "num1") String num1, @PathVariable(value = "num2") String num2) {
            
            if (!isNumeric(num1) || !isNumeric(num2)) {
                throw new UnsupportedOperationException("Please set a numeric value!");
            }
            return (convertToDouble(num1) + convertToDouble(num2)) / 2;
        }

        @GetMapping("/sqrt/{num1}")
        public Double sqrt(@PathVariable(value = "num1") String num1) {
            
            if (!isNumeric(num1)) {
                throw new UnsupportedOperationException("Please set a numeric value!");
            }
            return (Double) Math.sqrt(convertToDouble(num1));
        }

        private boolean isNumeric(String num1) {
            if (num1 == null) {
                return false;
            }
            String number = num1.toString().replace(",", ".");
            return number.matches("[-+]?[0-9]*\\.?[0-9]+");
        }

        private Double convertToDouble(String strNumber) {
            if (strNumber == null) {
                return 0D;
            }
            String number = strNumber.replaceAll(",", ".");
            if (isNumeric(number)) {
                return Double.parseDouble(number);
            }
            return 0D;
        }

}
