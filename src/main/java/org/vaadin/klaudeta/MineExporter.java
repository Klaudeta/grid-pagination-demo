package org.vaadin.klaudeta;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.DataCommunicator;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.function.ValueProvider;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MineExporter<T> {

    MineExporter(Grid<T> grid) {
        this.grid = Objects.requireNonNull(grid);
    }

    private Grid<T> grid;

    public void forGrid(Grid<T> grid){
        this.grid = grid;
    }

    private List<ValueProvider<T, ?>> valueProviders = new ArrayList<>();

    public <TARGET> MineExporter<T> withColumn(String header, ValueProvider<T, TARGET> valueProvider){
        valueProviders.add(valueProvider);
        return this;
    }


    private void buildRows() {
        Object filter = null;
        try {
            Method method = DataCommunicator.class.getDeclaredMethod("getFilter");
            method.setAccessible(true);
            filter = method.invoke(grid.getDataCommunicator());
        } catch (Exception e) {
            LoggerFactory.getLogger(this.getClass()).error("Unable to get filter from DataCommunicator");
        }

        Query streamQuery = new Query(0, grid.getDataProvider().size(new Query(filter)), grid.getDataCommunicator().getBackEndSorting(),
                grid.getDataCommunicator().getInMemorySorting(), null);
        Stream<T> dataStream = getDataStream(streamQuery);

        dataStream.forEach(t -> {
            valueProviders.forEach(v -> {
                Object value = v.apply(t);

            });
        });
    }

    private Stream<T> getDataStream(Query newQuery) {
        Stream<T> stream = grid.getDataProvider().fetch(newQuery);
        if (stream.isParallel()) {
            LoggerFactory.getLogger(DataCommunicator.class)
                    .debug("Data provider {} has returned "
                                    + "parallel stream on 'fetch' call",
                            grid.getDataProvider().getClass());
            stream = stream.collect(Collectors.toList()).stream();
            assert !stream.isParallel();
        }
        return stream;
    }
}
