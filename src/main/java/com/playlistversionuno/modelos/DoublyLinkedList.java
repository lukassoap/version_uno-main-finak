    /*
    * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
    * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
    */
    package com.playlistversionuno.modelos;

    import com.playlistversionuno.interphases.List;
    import java.util.Iterator;
    import java.util.ListIterator;
    import java.util.NoSuchElementException;

    /**
     *
     * @author yacog
     * must check how to make loop later and see if it works
     */
        public class DoublyLinkedList<E> implements List<E>, Iterable<E> {
        
        private Node<E> first; // referencia al primer nodo de la lista
        private Node<E> last; // referencia al ultimo nodo de la lista
        int current; // contador de nodos en la lista
        // definir clase nodo de manera inner porque solo se usa aqui

        class Node<E> {
            E data;
            Node<E> next; // referencia al siguiente nodo
            Node<E> prev; // referencia al nodo previo
            // Constructor del nodo
            Node(E data) 
            {
                this.data = data;
            }

        }
        @Override
        public boolean addFirst(E e) {
            if (e == null)
                return false;
            Node<E> newNode = new Node<>(e);
            if (isEmpty()) {
                first = last = newNode; // si la lista esta vacia, el nuevo nodo es tanto el primero como el ultimo
            } else {
                newNode.next = first; // el siguiente del nuevo nodo es el actual primero
                first.prev = newNode; // el previo del actual primero es el nuevo nodo
                first = newNode; // se actualiza el primer nodo
            }
            current++; // incrementamos el contador de nodos
            return true; // devolvemos true porque se agrego el nodo
        }

        @Override
        public boolean addLast(E e) {
            if (e == null)
                return false;
            Node<E> newNode = new Node<>(e);
            if (isEmpty()) {
                first = last = newNode; // si la lista esta vacia, el nuevo nodo es tanto el primero como el ultimo
            } else {
                last.next = newNode; // el siguiente del actual ultimo es el nuevo nodo
                newNode.prev = last; // el previo del nuevo nodo es el actual ultimo
                last = newNode; // se actualiza el ultimo nodo
            }
            current++; // incrementamos el contador de nodos
            return true;
        }

    @Override
    public boolean add(int index, E e) {
        if (index < 0 || index > current) throw new IndexOutOfBoundsException();
        if (e == null) return false;
        
        Node<E> newNode = new Node<>(e);
        if (index == 0) {
            return addFirst(e);
        } else if (index == current) {
            return addLast(e);
        } else {
            Node<E> temp = first;
            for (int i = 0; i < index - 1; i++) {
                temp = temp.next;
            }
            newNode.next = temp.next;
            newNode.prev = temp;
            temp.next.prev = newNode;
            temp.next = newNode;
            current++;
            return true;
        }
    }

        @Override
        public E removeFirst() {
            if (isEmpty())
                throw new IllegalStateException("List is empty");
            E temp = first.data; // guardamos el dato del primer nodo
            if (first == last) { // si hay un solo elemento
                first = last = null; // la lista queda vacia
            } else {
                first = first.next; // movemos el primer nodo al siguiente
                first.prev.next = null; // el nuevo primer nodo no tiene siguientekj
                first.prev = null; // el nuevo primer nodo no tiene previo

            }
            current--; // decrementamos el contador de nodos
            return temp; // devolvemos el dato del nodo eliminado

        }

        @Override
        public E removeLast() {
            if (isEmpty())
                throw new IllegalStateException("List is empty");
            E temp = last.data; // guardamos el dato del primer nodo
            if (first == last) { // si hay un solo elemento
                first = last = null; // la lista queda vacia
            } else {
                last = last.prev; // movemos el primer nodo al siguiente
                last.next.prev = null; // el nuevo primer nodo no tiene siguientekj
                last.prev = null; // el nuevo primer nodo no tiene previo

            }
            current--;
            return temp; // devolvemos el dato del nodo eliminado
        }

    public E getFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("List is empty");
        }
        return first.data;
    }


    @Override
    public E remove(int index) {
        if (index < 0 || index >= current) throw new IndexOutOfBoundsException();
        
        if (index == 0) {
            return removeFirst();
        } else if (index == current - 1) {
            return removeLast();
        } else {
            Node<E> temp = first;
            for (int i = 0; i < index; i++) {
                temp = temp.next;
            }
            temp.prev.next = temp.next;
            temp.next.prev = temp.prev;
            current--;
            return temp.data;
        }
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= current) {
            throw new IndexOutOfBoundsException();
        }
        Node<E> current = first;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }
        @Override
        public int size() {
            int count = 0;
            Node<E> current = first;
            while (current != null) {
                count++;
                current = current.next;
            }
            return count;
        }

        @Override
        public boolean isEmpty() {
            // TODO Auto-generated method stub
            // throw new UnsupportedOperationException("Unimplemented method 'isEmpty'");
            return first == null && last == null; // tambien puede ser current == 0
        }

        @Override
        public int index(E e) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'index'");
        }

            @Override
            public Iterator<E> iterator() 
            {
                Iterator<E> it= new Iterator<E>()
                { // implementando una clase anonima que esta implementando una interfaz
                    private Node<E> currentNode = first; // iniciamos el iterador en el primer nodo tambien se llama P o pointer

                    @Override
                    public boolean hasNext() 
                    {
                        return currentNode != null; // si hay un nodo siguiente
                    }

                    @Override
                    public E next() 
                    {
                        E temp = currentNode.data; // guardamos el dato del nodo actual
                        currentNode = currentNode.next; // movemos al siguiente nodo
                        return temp; // devolvemos el dato del nodo actual
                    }
                    

                };
                return it; // devolvemos el iterador
            }

            public ListIterator<E> listIterator(){ //changed into public
            ListIterator<E> lit = new ListIterator<E>() {
                Node<E> currentNode = first; // inicializa el nodo actual en null llamado tambien P

                @Override
                public boolean hasNext() {
                    return currentNode != null;
                }

                @Override
                public E next() {
                    E temp= currentNode.data; // guarda el dato del nodo actual
                    currentNode = currentNode.next; // mueve al siguiente nodo
                    if (currentNode == null)
                    {
                        currentNode= first;// aqui le digo si da null que sea vuelva a last
                        return temp;
                    }
                    else{
                        return temp;
                    }
                    //return temp; // devuelve el dato del nodo actual
                }

                @Override
                public boolean hasPrevious() {
                    return currentNode != null; // asi porque no usa nada mas no es sentinela 
                }

                @Override
                public E previous() {
                    E temp= currentNode.data; // guarda el dato del nodo actual
                    currentNode = currentNode.prev; // mueve al siguiente nodo
                    //voy a agregar un cambio
                    if (currentNode == null)
                    {
                        currentNode= last;// aqui le digo si da null que sea vuelva a last
                        return temp;
                    }
                    else{
                        return temp;
                    }
                    
                    
                }

                @Override
                public int nextIndex() {
                    // TODO Auto-generated method stub
                    throw new UnsupportedOperationException("Unimplemented method 'nextIndex'");
                }

                @Override
                public int previousIndex() {
                    // TODO Auto-generated method stub
                    throw new UnsupportedOperationException("Unimplemented method 'previousIndex'");
                }

                @Override
                public void remove() {
                    // TODO Auto-generated method stub
                    throw new UnsupportedOperationException("Unimplemented method 'remove'");
                }

                @Override
                public void set(E e) {
                    // TODO Auto-generated method stub
                    throw new UnsupportedOperationException("Unimplemented method 'set'");
                }

                @Override
                public void add(E e) {
                    // TODO Auto-generated method stub
                    throw new UnsupportedOperationException("Unimplemented method 'add'");
                }
                
                
            };
            return lit; // devuelve el list iterator
        }
    }
