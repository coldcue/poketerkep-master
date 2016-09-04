package hu.poketerkep.master.dataservice;


import java.util.Collection;

interface DataService<U> {

    void save(U obj);

    void delete(U obj);

    Collection<U> getAll();
}
