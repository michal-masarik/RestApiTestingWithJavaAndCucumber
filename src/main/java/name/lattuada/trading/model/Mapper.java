package name.lattuada.trading.model;

import org.modelmapper.ModelMapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public final class Mapper {

    private static ModelMapper modelMapper;

    static {
        modelMapper = new ModelMapper();
    }

    private Mapper() {
        // Cannot be instantiated
    }

    /**
     * Map object of type "S" (source) to object of type "R" (result)
     *
     * @param source
     * @param resultClass
     * @param <R>
     * @param <S>
     * @return
     */
    public static <R, S> R map(final S source, Class<R> resultClass) {
        return modelMapper.map(source, resultClass);
    }

    /**
     * Map a list of objects having type "S" (source) to a list of objects having type "R" (result)
     *
     * @param sourceList
     * @param resultClass
     * @param <R>
     * @param <S>
     * @return
     */
    public static <R, S> List<R> mapAll(final Collection<S> sourceList, Class<R> resultClass) {
        return sourceList.stream()
                .map(entity -> map(entity, resultClass))
                .collect(Collectors.toList());
    }

}
