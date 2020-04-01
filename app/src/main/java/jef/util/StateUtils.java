/*
 * Created on Jan 21, 2007
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package jef.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class StateUtils {
    
    public static void restoreState(ByteArray ba, Object o) {
        ArrayList fields = getFields(o);
        
        int idx = 0;
        
        for (int i = 0; i < fields.size(); i++) {
            Field f = (Field) fields.get(i);
            f.setAccessible(true);
            
            // skip static final
            int mod = f.getModifiers();
            if ((Modifier.isFinal(mod) && Modifier.isStatic(mod))) continue;
            
            // skip non-primitives/arrays
            Class c = f.getType();
            if (!(c.isPrimitive() || c.isArray())) continue;
            
            //System.out.println("Field name : " + f.getName() + ", type : " + c);
            
            if (c.isPrimitive()) {
                String ct = c.toString();
                if (ct.equals("int")) {
                    try {
                        f.setInt(o, ba.get32(idx));
                        idx+=4;
                    } catch (Exception e) {}
                }                
                if (ct.equals("short")) {
                    try {
                        f.setShort(o, (short) ba.get32(idx));
                        idx+=4;
                    } catch (Exception e) {}
                }                
                if (ct.equals("char")) {
                    try {
                        f.setChar(o, (char) ba.get16(idx));
                        idx+=2;
                    } catch (Exception e) {}
                }                
                if (ct.equals("byte")) {
                    try {
                        f.setByte(o, ba.get8(idx));
                        idx+=1;
                    } catch (Exception e) {}
                }                
                if (ct.equals("boolean")) {
                    try {
                        f.setBoolean(o, ba.get1(idx));
                        idx+=1;
                    } catch (Exception e) {}
                }    
                continue;
            }
            
            if (c.isArray()) {
                Class ac = c.getComponentType();
                if (ac.isPrimitive()) {
                    System.out.println("Component type : " + ac);
                    String act = ac.toString();
                    if (act.equals("int")) {
                        try {
                            int[] ic = (int[]) f.get(o);
                            for (int ii = 0; ii < ic.length; ii++) {
                                ic[ii] = ba.get32(idx);
                                idx+=4;
                            }
                        } catch (Exception e) {}
                    }
                    if (act.equals("short")) {
                        try {
                            short[] ic = (short[]) f.get(o);
                            for (int ii = 0; ii < ic.length; ii++) {
                                ic[ii] = (short) ba.get32(idx);
                                idx+=4;
                            }
                        } catch (Exception e) {}
                    }
                    if (act.equals("char")) {
                        try {
                            char[] ic = (char[]) f.get(o);
                            for (int ii = 0; ii < ic.length; ii++) {
                                ic[ii] = (char) ba.get16(idx);
                                idx+=2;
                            }
                        } catch (Exception e) {}
                    }
                    if (act.equals("byte")) {
                        try {
                            byte[] ic = (byte[]) f.get(o);
                            for (int ii = 0; ii < ic.length; ii++) {
                                ic[ii] = ba.get8(idx);
                                idx+=1;
                            }
                        } catch (Exception e) {}
                    }
                    if (act.equals("boolean")) {
                        try {
                            boolean[] ic = (boolean[]) f.get(o);
                            for (int ii = 0; ii < ic.length; ii++) {
                                ic[ii] = ba.get1(idx);
                                idx+=1;
                            }
                        } catch (Exception e) {}
                    }
                }                
            }
        }
        
    }
    
    public static ByteArray getState(Object o) {
        ByteArray ba = new ByteArray();
        
        ArrayList fields = getFields(o);
        
        for (int i = 0; i < fields.size(); i++) {
            Field f = (Field) fields.get(i);
            f.setAccessible(true);
            
            // skip static final
            int mod = f.getModifiers();
            if ((Modifier.isFinal(mod) && Modifier.isStatic(mod))) continue;
            
            // skip non-primitives/arrays
            Class c = f.getType();
            if (!(c.isPrimitive() || c.isArray())) continue;
            
            //System.out.println("Field name : " + f.getName() + ", type : " + c);
            
            if (c.isPrimitive()) {
                String ct = c.toString();
                if (ct.equals("int")) {
                    try {
                        System.out.println("adding");
                        ba.add32(f.getInt(o));
                    } catch (Exception e) {}
                }
                if (ct.equals("short")) {
                    try {
                        System.out.println("adding");
                        ba.add32(f.getInt(o));
                    } catch (Exception e) {}
                }
                if (ct.equals("char")) {
                    try {
                        System.out.println("adding");
                        ba.add16(f.getInt(o));
                    } catch (Exception e) {}
                }
                if (ct.equals("byte")) {
                    try {
                        System.out.println("adding");
                        ba.add8(f.getByte(o));
                    } catch (Exception e) {}
                }
                if (ct.equals("boolean")) {
                    try {
                        System.out.println("adding");
                        ba.add1(f.getBoolean(o));
                    } catch (Exception e) {}
                }
                continue;
            }
            
            if (c.isArray()) {
                Class ac = c.getComponentType();
                if (ac.isPrimitive()) {
                    System.out.println("Component type : " + ac);
                    String act = ac.toString();
                    if (act.equals("int")) {
                        try {
                            System.out.println("adding");
                            int[] ic = (int[]) f.get(o);
                            for (int ii = 0; ii < ic.length; ii++) {
                                ba.add32(ic[ii]);
                            }
                        } catch (Exception e) {}
                    }
                    if (act.equals("short")) {
                        try {
                            System.out.println("adding");
                            short[] ic = (short[]) f.get(o);
                            for (int ii = 0; ii < ic.length; ii++) {
                                ba.add32(ic[ii]);
                            }
                        } catch (Exception e) {}
                    }
                    if (act.equals("char")) {
                        try {
                            System.out.println("adding");
                            char[] ic = (char[]) f.get(o);
                            for (int ii = 0; ii < ic.length; ii++) {
                                ba.add16(ic[ii]);
                            }
                        } catch (Exception e) {}
                    }
                    if (act.equals("byte")) {
                        try {
                            System.out.println("adding");
                            byte[] ic = (byte[]) f.get(o);
                            for (int ii = 0; ii < ic.length; ii++) {
                                ba.add8(ic[ii]);
                            }
                        } catch (Exception e) {}
                    }
                    if (act.equals("boolean")) {
                        try {
                            System.out.println("adding");
                            boolean[] ic = (boolean[]) f.get(o);
                            for (int ii = 0; ii < ic.length; ii++) {
                                ba.add1(ic[ii]);
                            }
                        } catch (Exception e) {}
                    }
                }
            }
        }
        
        return ba;
    }
    
    private static ArrayList getFields(Object o) {
        Field[] f = o.getClass().getDeclaredFields();
        ArrayList al = new ArrayList();
        for (int i = 0; i < f.length; i++) {
            al.add(f[i]);
        }
        Collections.sort(al, new Comparator() {
            public int compare(Object o1, Object o2) {
                String s1 = ((Field)o1).getName();
                String s2 = ((Field)o2).getName();
                return s1.compareTo(s2);
            }
        });
        return al;
    }

}
