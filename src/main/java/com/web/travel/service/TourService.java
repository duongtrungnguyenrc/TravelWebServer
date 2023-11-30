package com.web.travel.service;

import com.web.travel.dto.ResDTO;
import com.web.travel.dto.request.admin.tour.TourAddingDTO;
import com.web.travel.dto.response.ListTourResDTO;
import com.web.travel.dto.response.TourDetailResDTO;
import com.web.travel.dto.response.TourGeneralResDTO;
import com.web.travel.mapper.Mapper;
import com.web.travel.mapper.request.TourAddingReqMapper;
import com.web.travel.mapper.request.TourParagraphsAddingMapper;
import com.web.travel.mapper.response.TourDetailResMapper;
import com.web.travel.mapper.response.TourGeneralResMapper;
import com.web.travel.model.*;
import com.web.travel.model.enumeration.EOrderStatus;
import com.web.travel.model.enumeration.EStatus;
import com.web.travel.model.enumeration.ETourType;
import com.web.travel.repository.*;
import com.web.travel.repository.custom.CustomTourRepository;
import com.web.travel.repository.custom.enumeration.ESortType;
import com.web.travel.service.cloudinary.FileUploadServiceImpl;
import com.web.travel.service.cloudinary.FilesValidation;
import com.web.travel.utils.DateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class TourService {
    @Autowired
    TourRepository tourRepository;
    @Autowired
    TourDateRepository tourDateRepository;
    @Autowired
    ParagraphRepository paragraphRepository;
    @Autowired
    TourBlogRepository tourBlogRepository;
    @Autowired
    CustomTourRepository customTourRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    FileUploadServiceImpl fileUploadService;
    @Autowired
    UserService userService;
    @Autowired
    TourAddingReqMapper tourAddingRequestMapper;
    @Autowired
    BlogRepository blogRepository;
    @Autowired
    FilesValidation filesValidation;
    public List<ListTourResDTO> getTourDTOListGroupByType(){
        List<ListTourResDTO> listTourResDTOS = new ArrayList<>();
        List<TourGeneralResDTO> list = new ArrayList<>();

        ListTourResDTO dto1 = new ListTourResDTO();
        dto1.setType("Popular");
        dto1.setTours(tourRepository.findByTourType(ETourType.TYPE_POPULAR)
                .stream()
                .filter(tour -> (tour.getIsRemoved() == null || !tour.getIsRemoved()))
                .map(
                        tour -> {
                            TourGeneralResMapper mapper = new TourGeneralResMapper();
                            return (TourGeneralResDTO) mapper.mapToDTO(tour);
                        }
                ).toList());

        ListTourResDTO dto2 = new ListTourResDTO();
        dto2.setType("Normal");
        dto2.setTours(tourRepository.findByTourType(ETourType.TYPE_NORMAL)
                .stream()
                .filter(tour -> (tour.getIsRemoved() == null || !tour.getIsRemoved()))
                .map(
                        tour -> {
                            TourGeneralResMapper mapper = new TourGeneralResMapper();
                            return (TourGeneralResDTO) mapper.mapToDTO(tour);
                        }
                ).toList());

        ListTourResDTO dto3 = new ListTourResDTO();
        dto3.setType("Special");
        dto3.setTours(tourRepository.findByTourType(ETourType.TYPE_SPECIAL)
                .stream()
                .filter(tour -> (tour.getIsRemoved() == null || !tour.getIsRemoved()))
                .map(
                        tour -> {
                            TourGeneralResMapper mapper = new TourGeneralResMapper();
                            return (TourGeneralResDTO) mapper.mapToDTO(tour);
                        }
                ).toList());

        ListTourResDTO dto4 = new ListTourResDTO();
        dto4.setType("Saving");
        dto4.setTours(tourRepository.findByTourType(ETourType.TYPE_SAVING)
                .stream()
                .filter(tour -> (tour.getIsRemoved() == null || !tour.getIsRemoved()))
                .map(
                        tour -> {
                            TourGeneralResMapper mapper = new TourGeneralResMapper();
                            return (TourGeneralResDTO) mapper.mapToDTO(tour);
                        }
                ).toList());

        listTourResDTOS.add(dto1);
        listTourResDTOS.add(dto2);
        listTourResDTOS.add(dto3);
        listTourResDTOS.add(dto4);

        return listTourResDTOS;
    }

    public Map<String, Object> getAllTour(int page, int limit){
        Map<String, Object> result = new HashMap<>();
        Page<Tour> tourPage = tourRepository.findAll(PageRequest.of(page, limit));
        List<TourGeneralResDTO> tours = tourPage.stream()
                .filter(tour -> (tour.getIsRemoved() == null || !tour.getIsRemoved()))
                .map(tour -> {
                    Mapper tourMapper = new TourGeneralResMapper();
                    return (TourGeneralResDTO) tourMapper.mapToDTO(tour);
                }).toList();

        result.put("tours", tours);
        int pages = tourPage.getTotalPages();
        result.put("pages", pages);
        return result;
    }

    public Tour findTourById(Long id){
        return tourRepository.findById(id).orElse(null);
    }

    public List<TourGeneralResDTO> findTourByType(String type){

        return tourRepository.findByTourType(ETourType.valueOf(("type_" + type).toUpperCase())).stream()
                .filter(tour -> (tour.getIsRemoved() == null || !tour.getIsRemoved()))
                .map(
                        tour -> {
                            Mapper mapper = new TourGeneralResMapper();
                            return (TourGeneralResDTO) mapper.mapToDTO(tour);
                        }
                ).toList();
    }

    public long getCount(){
        return tourRepository.count();
    }
    public Object getResponseTourById(Principal principal, Long id){
        Tour tour = tourRepository.findById(id).orElse(null);
        if(tour != null && (tour.getIsRemoved() == null || !tour.getIsRemoved())){
            List<TourGeneralResDTO> relevantTours = getRelevantToursByDestination(tour.getDepart(), tour.getDestination(), 5);
            relevantTours = relevantTours.stream().filter(relevantTour -> !Objects.equals(tour.getId(), relevantTour.getId())).toList();
            Mapper mapper = new TourDetailResMapper();
            Map<String, Object> result = new HashMap<>();
            ArrayList<TourDate> tourDates = new ArrayList<>(tour.getTourDate());

            sortTourDateByDepartDate(tourDates, ESortType.TYPE_ASC);
            List<TourDate> newTourDate = new ArrayList<>(tourDates);
            tour.setTourDate(newTourDate);

            sortTourDateByPrice(tourDates, ESortType.TYPE_ASC);
            double priceStartFrom = tourDates.stream().findFirst().orElse(new TourDate()).getChildPrice();

            TourBlog tourBlog = tourBlogRepository.findByTour(tour).orElse(new TourBlog());
            Blog blog = tourBlog.getBlog();
            List<Paragraph> paragraphs = new ArrayList<>();
            if(blog != null)
                paragraphs = (List<Paragraph>) blog.getParagraphs();
            paragraphs.sort(new Comparator<Paragraph>() {
                @Override
                public int compare(Paragraph o1, Paragraph o2) {
                    if(o1.getOrder() == null || o2.getOrder() == null)
                        return 0;
                    return o1.getOrder() - o2.getOrder();
                }
            });
            List<Schedule> schedules = (List<Schedule>) tour.getSchedules();

            result.put("blog", blog);
            result.put("tour", tour);
            result.put("tourBlog", tourBlog);
            result.put("paragraphs", paragraphs);
            result.put("schedules", schedules);
            result.put("relevantTours", relevantTours);
            result.put("price", priceStartFrom);

            TourDetailResDTO response = (TourDetailResDTO) mapper.mapToDTO(result);
            response.setRatingAcceptance(false);

            if(principal != null){
                User foundUser = userService.getUserObjectByEmail(principal.getName());
                List<Order> userOrders = orderRepository.findByUserAndTour(foundUser, tour);

                for (Order order : userOrders) {
                    if((order.getTourDate().getDepartDate().getTime() <= DateHandler.getCurrentDateTime().getTime()) &&
                            order.getStatus() == EOrderStatus.STATUS_ORDERED){
                        response.setRatingAcceptance(true);
                        break;
                    }
                }
            }
            return response;
        }
        return null;
    }

    public List<TourGeneralResDTO> getRelevantToursByDestination(String depart, String destination , int numberOfTour){
        List<TourGeneralResDTO> result;
        List<Tour> relevantTours = customTourRepository.getRelevantTourByDestination(depart, destination, numberOfTour);
        result = relevantTours.stream()
                .filter(tour -> (tour.getIsRemoved() == null || !tour.getIsRemoved()))
                .map(tour -> {
                    Mapper mapper = new TourGeneralResMapper();
                    return (TourGeneralResDTO) mapper.mapToDTO(tour);
                }).toList();
        return result;
    }

    private static void sortTourDateByPrice(ArrayList<TourDate> tourDates, ESortType sortType){
        if(sortType.equals(ESortType.TYPE_ASC)){
            tourDates.sort((o1, o2) -> {
                if(o1.getChildPrice() < o2.getChildPrice())
                    return -1;
                else if(o1.getChildPrice() == o2.getChildPrice())
                    return 0;
                return 1;
            });
        }else{
            tourDates.sort((o1, o2) -> {
                if(o1.getChildPrice() < o2.getChildPrice())
                    return 1;
                else if(o1.getChildPrice() == o2.getChildPrice())
                    return 0;
                return -1;
            });
        }
    }

    private static void sortTourDateByDepartDate(ArrayList<TourDate> tourDates, ESortType sortType){
        if(sortType.equals(ESortType.TYPE_ASC)){
            tourDates.sort((o1, o2) -> {
                if(o1.getDepartDate().getTime() < o2.getDepartDate().getTime())
                    return -1;
                else if(o1.getDepartDate().getTime() == o2.getDepartDate().getTime())
                    return 0;
                return 1;
            });
        }else{
            tourDates.sort((o1, o2) -> {
                if(o1.getDepartDate().getTime() < o2.getDepartDate().getTime())
                    return 1;
                else if(o1.getDepartDate().getTime() == o2.getDepartDate().getTime())
                    return 0;
                return -1;
            });
        }
    }

    public ResDTO add(TourAddingDTO tour, MultipartFile[] images){
        Mapper paraMapper = new TourParagraphsAddingMapper();
        Tour needAddTour = (Tour) tourAddingRequestMapper.mapToObject(tour);
        List<Paragraph> needAddParagraphs = (List<Paragraph>) paraMapper.mapToObject(tour);

        Tour addedTour = tourRepository.save(needAddTour);

        String thumbnailName = null;
        List<String> imageNames = new ArrayList<>();
        try {
            imageNames = fileUploadService.uploadMultiFile(images);
            thumbnailName = imageNames.get(0);
        }catch (IOException e) {
            System.out.println(e.getMessage());
        }
        needAddTour.setImg(thumbnailName);

        Blog blog = new Blog();
        needAddParagraphs.forEach(para -> {
            para.setBlog(blog);
        });
        blog.setBackgroundImg(thumbnailName);
        blog.setParagraphs(needAddParagraphs);

        TourBlog tourBlog = new TourBlog();
        tourBlog.setTour(addedTour);
        tourBlog.setBlog(blog);

        TourBlog addedTourBlog = tourBlogRepository.save(tourBlog);

        Blog addedBlog = addedTourBlog.getBlog();
        List<Paragraph> addedParagraph = addedBlog
                .getParagraphs()
                .stream().toList();

        if(imageNames.size() - 1 == needAddParagraphs.size()){
            for (int i = 0; i < imageNames.size() - 1; i++){
                addedParagraph.get(i).setImgName(tour.getParagraphs().get(i).getImageName());
                addedParagraph.get(i).setImgSrc(imageNames.get(i + 1));
                paragraphRepository.save(addedParagraph.get(i));
            }
        }

        Mapper mapper = new TourGeneralResMapper();
        TourGeneralResDTO response = (TourGeneralResDTO) mapper.mapToDTO(addedTour);

        return new ResDTO(
                200,
                true,
                "Thêm tour thành công!",
                response
            );
    }

    public ResDTO updateTour(
            long id, TourAddingDTO tour, MultipartFile[] images
    ){
        AtomicReference<Tour> updatedTour = new AtomicReference<>(null);

        tourRepository.findById(id).ifPresent(foundTour -> {
            if(foundTour.getIsRemoved() == null || !foundTour.getIsRemoved()){
                Mapper paraMapper = new TourParagraphsAddingMapper();
                Tour needUpdateTour = (Tour) tourAddingRequestMapper.mapToObject(tour);
                List<Paragraph> newParagraphs = (List<Paragraph>) paraMapper.mapToObject(tour);

                String thumbnailName = null;
                List<String> paragraphImages = new ArrayList<>();
                try {
                    if (filesValidation.validate(images) != EStatus.STATUS_EMPTY_FILE){
                        paragraphImages = fileUploadService.uploadMultiFile(images);

                        thumbnailName = paragraphImages.get(0);
                    }
                }catch (IOException e) {
                    System.out.println(e.getMessage());
                }

                TourBlog needUpdateTourBlog = tourBlogRepository.findByTour(foundTour).orElse(null);

                if(needUpdateTourBlog != null){
                    if(thumbnailName != null)
                        needUpdateTourBlog.getBlog().setBackgroundImg(thumbnailName);
                    needUpdateTourBlog.getBlog().getParagraphs().clear();
                    newParagraphs.forEach(paragraph -> {
                        paragraph.setBlog(needUpdateTourBlog.getBlog());
                    });

                    tourBlogRepository.save(needUpdateTourBlog);
                }

                if(paragraphImages.size() - 1 == newParagraphs.size()){
                    for(int i = 0; i < newParagraphs.size(); i++){
                        if(i + 1 < paragraphImages.size() && paragraphImages.get(i + 1) != null){
                            newParagraphs.get(i).setImgSrc(paragraphImages.get(i + 1));
                        }else{
                            newParagraphs.get(i).setImgSrc(
                                    tour.getParagraphs().get(i).getOldImage()
                            );
                        }
                        paragraphRepository.save(newParagraphs.get(i));
                    }
                }

                foundTour.setName(needUpdateTour.getName());
                foundTour.setVehicle(needUpdateTour.getVehicle());
                foundTour.setTourType(needUpdateTour.getTourType());
                foundTour.setDepart(needUpdateTour.getDepart());
                foundTour.setDestination(needUpdateTour.getDestination());
                if(thumbnailName != null)
                    foundTour.setImg(thumbnailName);

                needUpdateTour.getTourDate().forEach(tourDate -> {
                    tourDate.setTour(foundTour);
                    foundTour.getTourDate().add(tourDate);
                });

                foundTour.getSchedules().clear();

                needUpdateTour.getSchedules().forEach(schedule -> {
                    schedule.setTour(foundTour);
                    foundTour.getSchedules().add(schedule);
                });

                foundTour.setHotels(needUpdateTour.getHotels());

                updatedTour.set(tourRepository.save(foundTour));
            }
        });

        TourGeneralResMapper mapper = new TourGeneralResMapper();
        TourGeneralResDTO response = (TourGeneralResDTO) mapper.mapToDTO(updatedTour.get());

        String message = "Chỉnh sửa tour thành thông!";

        if (updatedTour.get() == null){
            message = "Tour không tìm thấy!";
        }

        return new ResDTO(
                200,
                true,
                message,
                response
        );
    }

    public ResDTO deleteTour(long id){

        AtomicReference<String> message = new AtomicReference<>("Không tìm thấy tour với id: " + id);

        tourRepository.findById(id).ifPresent(tour -> {
            tour.setIsRemoved(true);
            tourRepository.save(tour);
            message.set("Xóa tour thành công!");
        });

        Map<String, Long> response = new HashMap<>();
        response.put("deletedId", id);
        return new ResDTO(
                200,
                true,
                message.get(),
                response
        );
    }
}
