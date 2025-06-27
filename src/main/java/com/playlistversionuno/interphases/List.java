/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.playlistversionuno.interphases;

/**
 *
 * @author yacog
 */
public interface List<E> {
    boolean addFirst(E e); // define que tipo de dato y variable se pone ahi cunado creamos la lista
    // se decide hacer el dato parametrizado para que se pueda usar con cualquier tipo de dato
    boolean addLast(E e);
    boolean add(int index, E e); //se pone boolean porque se retorna si se pudo agregar o no
    //se falat definir el tipo de dato de retorno, en este caso no se retorna nada
    // se pone al lado de addfirst y add last el boolean en este caso por eleccion
    //ahora ponemos remove first and remove last 
    E removeFirst();
    E removeLast();
    //ponemos que no necesitamos una variable que reciba ya que solo elimina el ultimo y final si se llama
    // se pone E porque no se retorna nada, solo se elimina el ultimo y el primero y queremos que se sepa que elimina ded todo 
    //pero si fuera una lista de ints o strings, se pondria el tipo de dato que se quiera
    E remove(int index); //no se usa E porque ya nos da el indice y no se necesita nada mas para encontrar
    E get(int index); //se pone E porque se retorna el dato que se encuentra en el indice
    int size(); //se pone int porque se retorna el tamaño de la lista
    boolean isEmpty(); //se pone boolean porque se retorna si la lista esta vacia o no
    //se usa el boolean en algunos casos para por ejemplo comprbar si se pudo obtener 
    int index(E e); //se pone E porque se retorna el dato que se encuentra en el indice

}
