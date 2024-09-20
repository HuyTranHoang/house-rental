package com.project.house.rental.service.impl;

import com.project.house.rental.dto.UserMembershipDto;
import com.project.house.rental.entity.Membership;
import com.project.house.rental.entity.UserMembership;
import com.project.house.rental.entity.auth.UserEntity;
import com.project.house.rental.mapper.UserMembershipMapper;
import com.project.house.rental.repository.MembershipRepository;
import com.project.house.rental.repository.UserMembershipRepository;
import com.project.house.rental.repository.auth.UserRepository;
import com.project.house.rental.security.JWTTokenProvider;
import com.project.house.rental.service.UserMembershipService;
import com.project.house.rental.specification.UserMembershipSpecifition;
import com.project.house.rental.utils.HibernateFilterHelper;
import jakarta.persistence.NoResultException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class UserMemberShipServiceImpl implements UserMembershipService {
    private final HibernateFilterHelper hibernateFilterHelper;
    private final UserMembershipRepository userMembershipRepository;
    private final MembershipRepository membershipRepository;
    private final UserMembershipMapper userMembershipMapper;
    private final UserRepository userRepository;
    private final JWTTokenProvider jwtTokenProvider;

    public UserMemberShipServiceImpl(HibernateFilterHelper hibernateFilterHelper, UserMembershipRepository userMembershipRepository, MembershipRepository membershipRepository, UserMembershipMapper userMembershipMapper, UserRepository userRepository, JWTTokenProvider jwtTokenProvider) {
        this.hibernateFilterHelper = hibernateFilterHelper;
        this.userMembershipRepository = userMembershipRepository;
        this.membershipRepository = membershipRepository;
        this.userMembershipMapper = userMembershipMapper;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public List<UserMembershipDto> getAllUserMemberships(long membershipId) {
        Specification<UserMembership> spec = UserMembershipSpecifition.filterByMembership(membershipId);

        //hibernateFilterHelper.enableFilter(FilterConstant.DELETE_USER_MEMBERSHIP_FILTER);

        List<UserMembership> userMemberships = userMembershipRepository.findAll(spec);

        //hibernateFilterHelper.disableFilter(FilterConstant.DELETE_USER_MEMBERSHIP_FILTER);

        return userMemberships.stream()
                .map(userMembershipMapper::toDto)
                .toList();
    }

    @Override
    public UserMembershipDto getUserMembershipById(long id) {
        return null;
    }

    @Override
    public UserMembershipDto createUserMembership(long userId) {
        UserEntity currentUser = userRepository.findUserById(userId);

        if (currentUser == null) {
            throw new UsernameNotFoundException("Không tìm thấy tài khoản với userId: " + userId);
        }

        Membership membership = membershipRepository.findByNameIgnoreCase("Free");
        if (membership == null) {
            throw new RuntimeException("Không tìm thấy hạng mức thành viên FREE");
        }

        UserMembership userMembership = new UserMembership();

        userMembership.setUser(currentUser);
        userMembership.setMembership(membership);
        userMembership.setTotalPriorityLimit(membership.getPriority());
        userMembership.setTotalRefreshLimit(membership.getRefresh());

        return userMembershipMapper.toDto(userMembershipRepository.save(userMembership));
    }

    @Override
    public UserMembershipDto updateUserMembership(UserMembershipDto userMembershipDto) {
        UserMembership userMembership = userMembershipRepository.findByUserId(userMembershipDto.getUserId());
        if (userMembership == null) {
            throw new NoResultException("Không tìm thấy UserMembership!");
        }

        Membership membership = membershipRepository.findById(userMembershipDto.getMembershipId())
                .orElseThrow(() -> new NoResultException("Không tìm thấy Membership"));

        // Kiểm tra trùng Membership ==> Gia hạng
        if ((userMembership.getMembership().getId()) == (userMembershipDto.getMembershipId())) {
            userMembership.setEndDate(Date.from(userMembership.getEndDate().toInstant().plus(Duration.ofDays(membership.getDurationDays()))));
        } else {
            // Kiểm tra khác Membership ==> Nâng hạng
            userMembership.setMembership(membership);
            userMembership.setStartDate(new Date());
            userMembership.setEndDate(Date.from(LocalDate.now().plusDays(membership.getDurationDays()).atStartOfDay(ZoneId.systemDefault()).toInstant()));
            userMembership.setStatus(UserMembership.Status.ACTIVE);
        }

        // Cộng giá trị của gói vào các giới hạn
        userMembership.setTotalPriorityLimit(userMembership.getTotalPriorityLimit() + membership.getPriority());
        userMembership.setTotalRefreshLimit(userMembership.getTotalRefreshLimit() + membership.getRefresh());

        UserMembership updatedUserMembership = userMembershipRepository.save(userMembership);

        return userMembershipMapper.toDto(updatedUserMembership);
    }

    @Override
    public UserMembershipDto getUserMembershipByUsername(String username) {
        return null;
    }


}
