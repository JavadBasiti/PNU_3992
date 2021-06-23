package com.barshid.schematech.web.service;

import com.barshid.schematech.web.Token;
import com.barshid.schematech.web.dto.ResultEntery;
import com.barshid.schematech.web.dto.UserTO;
import com.barshid.schematech.web.infra.model.Filter;
import com.github.adminfaces.template.exception.BusinessException;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.github.adminfaces.template.util.Assert.has;

@Service
//@Transactional
public class PatientService {
    @Autowired
    private WebClient webClient;
    @Autowired
    private Token token;


    List<UserTO> allPatient = new ArrayList<>();
    public void loadPatients(){
        Flux<UserTO> flux = getList();
        allPatient=flux.collectSortedList().block();
    }

    public List<ResultEntery> loadResults(Long patientId, String qCode){
        Flux<ResultEntery> fListRTO= webClient.get()
                .uri("questionnaire/"+patientId+"/results/"+qCode)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + token.getAccess_token())
                .retrieve()
                .bodyToFlux(ResultEntery.class);
        return fListRTO.collectSortedList().block();
    }

    private Flux<UserTO> getList() {
        return webClient.get()
                .uri("therapist/patients")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + token.getAccess_token())
                .retrieve()
                .bodyToFlux(UserTO.class);
    }

    public List<UserTO> listBySkiness(String model) {
        return allPatient.stream()
                .filter(c -> c.getSickness().equalsIgnoreCase(model))
                .collect(Collectors.toList());
    }

    public List<UserTO> paginate(Filter<UserTO> filter) {
        List<UserTO> pagedPatients = new ArrayList<>();
        if(has(filter.getSortOrder()) && !SortOrder.UNSORTED.equals(filter.getSortOrder())) {
            pagedPatients = allPatient.stream().
                    sorted((c1, c2) -> {
                        if (filter.getSortOrder().isAscending()) {
                            return c1.getId().compareTo(c2.getId());
                        } else {
                            return c2.getId().compareTo(c1.getId());
                        }
                    })
                    .collect(Collectors.toList());
        }

        int page = filter.getFirst() + filter.getPageSize();
        if (filter.getParams().isEmpty()) {
            pagedPatients = pagedPatients.subList(filter.getFirst(), page > allPatient.size() ? allPatient.size() : page);
            return pagedPatients;
        }

        List<Predicate<UserTO>> predicates = configFilter(filter);

        List<UserTO> pagedList = allPatient.stream().filter(predicates
                .stream().reduce(Predicate::or).orElse(t -> true))
                .collect(Collectors.toList());

        if (page < pagedList.size()) {
            pagedList = pagedList.subList(filter.getFirst(), page);
        }

        if (has(filter.getSortField())) {
            pagedList = pagedList.stream().
                    sorted((c1, c2) -> {
                        boolean asc = SortOrder.ASCENDING.equals(filter.getSortOrder());
                        if (asc) {
                            return c1.getId().compareTo(c2.getId());
                        } else {
                            return c2.getId().compareTo(c1.getId());
                        }
                    })
                    .collect(Collectors.toList());
        }
        return pagedList;
    }

    private List<Predicate<UserTO>> configFilter(Filter<UserTO> filter) {
        List<Predicate<UserTO>> predicates = new ArrayList<>();
        if (filter.hasParam("id")) {
            Predicate<UserTO> idPredicate = (UserTO c) -> c.getId().equals(filter.getParam("id"));
            predicates.add(idPredicate);
        }

        if (filter.hasParam("minAge") && filter.hasParam("maxAge")) {
            Predicate<UserTO> minMaxAgePredicate = (UserTO c) -> c.getAge()
                    >= Double.valueOf((String) filter.getParam("minAge")) && c.getAge()
                    <= Double.valueOf((String) filter.getParam("maxAge"));
            predicates.add(minMaxAgePredicate);
        } else if (filter.hasParam("minAge")) {
            Predicate<UserTO> minAgePredicate = (UserTO c) -> c.getAge()
                    >= Double.valueOf((String) filter.getParam("minAge"));
            predicates.add(minAgePredicate);
        } else if (filter.hasParam("maxAge")) {
            Predicate<UserTO> maxAgePredicate = (UserTO c) -> c.getAge()
                    <= Double.valueOf((String) filter.getParam("maxAge"));
            predicates.add(maxAgePredicate);
        }

        if (has(filter.getEntity())) {
            UserTO filterEntity = filter.getEntity();
            if (has(filterEntity.getSickness())) {
                Predicate<UserTO> modelPredicate = (UserTO c) -> c.getSickness().toLowerCase().contains(filterEntity.getSickness().toLowerCase());
                predicates.add(modelPredicate);
            }

            if (has(filterEntity.getAge())) {
                Predicate<UserTO> AgePredicate = (UserTO c) -> c.getAge().equals(filterEntity.getAge());
                predicates.add(AgePredicate);
            }

            if (has(filterEntity.getLastName())) {
                Predicate<UserTO> namePredicate = (UserTO c) -> c.getLastName().toLowerCase().contains(filterEntity.getLastName().toLowerCase());
                predicates.add(namePredicate);
            }
        }
        return predicates;
    }



    public long count(Filter<UserTO> filter) {
        return allPatient.stream()
                .filter(configFilter(filter).stream()
                        .reduce(Predicate::or).orElse(t -> true))
                .count();
    }


    public  UserTO findById(Long id) {
        return allPatient.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new BusinessException("patient not found with id " + id));
    }


    public void remove(UserTO selectedPatient) {
        allPatient.remove(selectedPatient);

    }

    public void setAllPatient(List<UserTO> allPatient) {
        this.allPatient = allPatient;
    }

    public List<String> getSicknesses(String query) {
        return null;
    }
}
