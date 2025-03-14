package com.bekaku.api.spring.serviceImpl;

import com.bekaku.api.spring.configuration.I18n;
import com.bekaku.api.spring.specification.SearchSpecification;
import com.bekaku.api.spring.dto.PermissionDto;
import com.bekaku.api.spring.dto.ResponseListDto;
import com.bekaku.api.spring.vo.Paging;
import com.bekaku.api.spring.mapper.PermissionMapper;
import com.bekaku.api.spring.model.Permission;
import com.bekaku.api.spring.repository.PermissionRepository;
import com.bekaku.api.spring.repository.PermissionRepositoryCustom;
import com.bekaku.api.spring.service.PermissionService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    @PersistenceContext
    private EntityManager entityManager;

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;
    private final ModelMapper modelMapper;
    private final I18n i18n;

    private final PermissionRepositoryCustom permissionRepositoryCustom;

    private static final String I18N_PREFIX = "permission.";

    @Transactional(readOnly = true)
    @Override
    public ResponseListDto<PermissionDto> findAllWithPaging(Pageable pageable) {
        Page<Permission> result = permissionRepository.findAll(pageable);
        return getListFromResult(result);
    }

    @Transactional(readOnly = true)
    @Override
    public ResponseListDto<PermissionDto> findAllWithSearch(SearchSpecification<Permission> specification, Pageable pageable) {
        Page<Permission> result = permissionRepository.findAll(specification, pageable);
        return getListFromResult(result);
    }

    @Override
    public ResponseListDto<PermissionDto> findAllBy(Specification<Permission> specification, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Permission> findAllPageSpecificationBy(Specification<Permission> specification, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Permission> findAllPageSearchSpecificationBy(SearchSpecification<Permission> specification, Pageable pageable) {
        return null;
    }

    private ResponseListDto<PermissionDto> getListFromResult(Page<Permission> result) {
        return new ResponseListDto<>(result.getContent()
                .stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList())
                , result.getTotalPages(), result.getTotalElements(), result.isLast());
    }

    @Transactional(readOnly = true)
    @Override
    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }

    @Override
    public Permission save(Permission permission) {
        return permissionRepository.save(permission);
//        return permissionRepository.saveWithLogging(permission);
    }

    @Override
    public Permission update(Permission permission) {
        return permissionRepository.save(permission);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Permission> findById(Long id) {
        return permissionRepository.findById(id);
    }

    @Override
    public void delete(Permission permission) {
        permissionRepository.delete(permission);
    }

    @Override
    public void deleteById(Long id) {
        permissionRepository.deleteById(id);
    }

    @Override
    public PermissionDto convertEntityToDto(Permission permission) {
        PermissionDto dto = modelMapper.map(permission, PermissionDto.class);
        String message = i18n.getMessage(I18N_PREFIX + permission.getCode());
        if (message != null) {
            dto.setDescription(message);
        }
        return dto;
    }

    @Override
    public Permission convertDtoToEntity(PermissionDto permissionDto) {
        return modelMapper.map(permissionDto, Permission.class);
    }

    @Override
    public List<PermissionDto> findAllLikeByCode(String code, Pageable pageable) {
        return permissionRepository.findAllLikeByCode(code, pageable)
                .stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    //My Batis
    @Transactional(readOnly = true)
    @Override
    public List<Permission> findAllViaMapper(Paging page) {
        return permissionMapper.findAllWithPaging(page);
    }


    @Override
    public boolean isHasPermission(long userId, String permissionCode) {
        List<String> permissionList = permissionRepository.findPermissionsByUserIdAndPermissionCode(userId, permissionCode);
        return !permissionList.isEmpty();
    }

    @Override
    public List<String> findAllPermissionCodeByUserId(Long userId, boolean frontend) {
        if (frontend) {
            return permissionRepository.findAllFrontendPermissionCodeByUserId(userId);
        }
        return permissionRepository.findAllBackendPermissionCodeByUserId(userId);
    }


    @Override
    public Optional<Permission> findByCode(String code) {
        return permissionRepository.findByCode(code);
    }

    @Override
    public void saveAll(List<Permission> permissionList) {
        permissionRepository.saveAll(permissionList);
    }

    @Override
    public Page<Permission> findAllWithSearchSpecification(SearchSpecification<Permission> specification, Pageable pageable) {
        return permissionRepository.findAll(specification, pageable);
    }

    @Override
    public List<Long> findAllPermissionIdByRoleId(Long roleId) {
        return permissionRepository.findAllPermissionIdByRoleId(roleId);
    }

    @Override
    public List<PermissionDto> findAllBy(boolean frontEnd, Sort sort) {
        List<Permission> list = permissionRepository.findAllByfrontEnd(frontEnd, sort);
        return list.stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    @Override
    public Object[] findCustomById(Long id) {
        return permissionRepositoryCustom.findById(id);
    }

    @Override
    public List<Object[]> findAllCustom() {
        return permissionRepositoryCustom.findAll();
    }

    //custom JPA query
//    public List<Permission> findAllNativeByCrudTableAndActive(String curdTable, Boolean status) {
//        Query query = this.entityManager.createNativeQuery("select * from permission p where p.crud_table = :tableName AND p.status=:status ", Permission.class);
//        query.setParameter("tableName", curdTable);
//        query.setParameter("status", status);
//        return query.getResultList();
//    }
//    public List<Permission> findAllNativePaging(Page page) {
//        Query query = this.entityManager.createNativeQuery("select * from permission p limit :offset, :limit ", Permission.class);
//        query.setParameter("offset", page.getOffset());
//        query.setParameter("limit", page.getLimit());
//        System.out.println("getResultList : "+query.getResultList().size());
//        return query.getResultList();
//    }

//    public List<Object[]> findAllNativeByCustomObject() {
//        List<Object[]> list = this.entityManager.createNativeQuery("SELECT * FROM permission p WHERE p.status=:status ")
//                .setParameter("status", true) //positional parameter binding
//                .getResultList();
        /*
         for (Object[] objects : list) {
            Integer id=(Integer)objects[0];
            String name=(String)objects[1];
            String designation=(String)objects[2];
            System.out.println("Employee["+id+","+name+","+designation+"]");
         }
         */
//        return list;
//    }
}
