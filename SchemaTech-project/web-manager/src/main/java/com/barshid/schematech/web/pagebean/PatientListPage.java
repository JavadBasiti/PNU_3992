package com.barshid.schematech.web.pagebean;

import com.barshid.schematech.web.dto.ResultEntery;
import com.barshid.schematech.web.dto.UserTO;
import com.barshid.schematech.web.infra.model.Filter;
import com.barshid.schematech.web.service.PatientService;
import com.github.adminfaces.template.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static com.barshid.schematech.web.util.Utils.addDetailMessage;
@Slf4j
@Named
@ViewScoped
//@Data
public class PatientListPage implements Serializable {
//    private Flux<UserTO> patients;
    LazyDataModel<UserTO> patients;

    Long id;

    Filter<UserTO> filter = new Filter<>(new UserTO());

    List<UserTO> selectedPatients; //patient selected in checkbox column

    List<UserTO> filteredValue;// datatable filteredValue attribute (column filters)

    List<ResultEntery> selectedResult;

    @Autowired
    private PatientService patientService;

    public void loadPatients(){
        patientService.loadPatients();
    }
    public void loadResults(){
        log.debug("selected Id="+selectedPatients.get(0).getId());
        selectedResult = patientService.loadResults(selectedPatients.get(0).getId(),"YSQ–S3");
    }

    public void loadResultsId(Long id){
        log.debug("Long Id ="+id);
        selectedResult = patientService.loadResults(id,"YSQ–S3");
    }

    @PostConstruct
    public void initDataModel() {
        loadPatients();
        //patients = patientService.getList();//.bodyToMono(UserTO.class);
        patients = new LazyDataModel<UserTO>() {
            @Override
            public List<UserTO> load(int first, int pageSize,
                                     String sortField, SortOrder sortOrder,
                                     Map<String, FilterMeta> filters) {
                com.barshid.schematech.web.infra.model.SortOrder order = null;
                if (sortOrder != null) {
                    order = sortOrder.equals(SortOrder.ASCENDING) ? com.barshid.schematech.web.infra.model.SortOrder.ASCENDING
                            : sortOrder.equals(SortOrder.DESCENDING) ? com.barshid.schematech.web.infra.model.SortOrder.DESCENDING
                            : com.barshid.schematech.web.infra.model.SortOrder.UNSORTED;
                }
                filter.setFirst(first).setPageSize(pageSize)
                        .setSortField(sortField).setSortOrder(order)
                        .setParams(filters);
                List<UserTO> list = patientService.paginate(filter);
                setRowCount((int) patientService.count(filter));
                return list;
            }

            @Override
            public int getRowCount() {
                return super.getRowCount();
            }

            @Override
            public UserTO getRowData(String key) {
                return patientService.findById(new Long(key));
            }
        };
    }

        public void clear() {
            filter = new Filter<UserTO>(new UserTO());
        }

        public List<String> completeSickness(String query) {
            List<String> result = patientService.getSicknesses(query);
            return result;
        }

        public void finPatientsById(Long id) {
            if (id == null) {
                throw new BusinessException("Provide patients ID to load");
            }
            selectedPatients.add(patientService.findById(id));
        }

        public void delete() {
            int numCars = 0;
            for (UserTO selectedPatients : selectedPatients) {
                numCars++;
                patientService.remove(selectedPatients);
            }
            selectedPatients.clear();
            addDetailMessage(numCars + " patient deleted successfully!");
        }

//    public Flux<UserTO> getPatients() {
//        return patients;
//    }
//
//    public void setPatients(Flux<UserTO> patients) {
//        this.patients = patients;
//    }

    public LazyDataModel<UserTO> getPatients() {
        return patients;
    }

    public void setPatients(LazyDataModel<UserTO> patients) {
        this.patients = patients;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Filter<UserTO> getFilter() {
        return filter;
    }

    public void setFilter(Filter<UserTO> filter) {
        this.filter = filter;
    }

    public List<UserTO> getSelectedPatients() {
        return selectedPatients;
    }

    public List<ResultEntery> getSelectedResult() {
        return selectedResult;
    }

    public void setSelectedResult(List<ResultEntery> selectedResult) {
        this.selectedResult = selectedResult;
    }

    public void setSelectedPatients(List<UserTO> selectedPatients) {
        this.selectedPatients = selectedPatients;
    }

    public List<UserTO> getFilteredValue() {
        return filteredValue;
    }

    public void setFilteredValue(List<UserTO> filteredValue) {
        this.filteredValue = filteredValue;
    }

}
