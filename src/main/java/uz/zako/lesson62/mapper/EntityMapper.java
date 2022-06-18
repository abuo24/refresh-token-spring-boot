package uz.zako.lesson62.mapper;


import java.util.List;

public interface EntityMapper<D, E> {

    D toDto(E e);

    E toEntity(D d);

    List<E> toEntity(List<D> d);
    List<D> toDto(List<E> e);



}
