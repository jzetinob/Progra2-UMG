/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.josue.ventas.dao;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author josue zetino
 */
public class CsvUtil {

    private CsvUtil() {
    }

    public static String escape(String campo) {
        if (campo == null) {
            return "";
        }
        if (campo.contains(";") || campo.contains("\"") || campo.contains("\n") || campo.contains("\r")) {
            return "\"" + campo.replace("\"", "\"\"") + "\"";
        }
        return campo;
    }

    public static String[] dividirLinea(String linea) {
        List<String> campos = new ArrayList<>();
        StringBuilder actual = new StringBuilder();
        boolean dentroComillas = false;
        for (int i = 0; i < linea.length(); i++) {
            char c = linea.charAt(i);
            if (dentroComillas) {
                if (c == '"') {
                    if (i + 1 < linea.length() && linea.charAt(i + 1) == '"') {
                        actual.append('"');
                        i++;
                    } else {
                        dentroComillas = false;
                    }
                } else {
                    actual.append(c);
                }
            } else if (c == '"') {
                dentroComillas = true;
            } else if (c == ';') {
                campos.add(actual.toString());
                actual.setLength(0);
            } else {
                actual.append(c);
            }
        }
        campos.add(actual.toString());
        return campos.toArray(new String[0]);
    }
}
