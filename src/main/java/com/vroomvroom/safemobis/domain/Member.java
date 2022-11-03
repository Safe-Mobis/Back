package com.vroomvroom.safemobis.domain;

import com.vroomvroom.safemobis.domain.enumerate.TrafficCode;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

import static com.vroomvroom.safemobis.domain.enumerate.TrafficCode.*;
import static com.vroomvroom.safemobis.domain.enumerate.TrafficCode.MOTORCYCLE;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Member extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TrafficCode trafficCode;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @Builder.Default
    private List<TrafficMode> trafficModes = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "position_id")
    private Position position;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setTrafficModes(List<TrafficMode> trafficModes) {
        this.trafficModes = trafficModes;
    }

    public Map<TrafficCode, Boolean> getTrafficWarningMap() throws Exception {
        TrafficMode trafficMode = getCurrentTrafficMode();
        Map<TrafficCode, Boolean> warningMap = new HashMap<>();
        warningMap.put(CAR, trafficMode.isCarFlag());
        warningMap.put(PEDESTRIAN, trafficMode.isPedestrianFlag());
        warningMap.put(CHILD, trafficMode.isChildFlag());
        warningMap.put(KICK_BOARD, trafficMode.isKickBoardFlag());
        warningMap.put(BICYCLE, trafficMode.isBicycleFlag());
        warningMap.put(MOTORCYCLE, trafficMode.isMotorcycleFlag());
        return warningMap;
    }

    public TrafficMode getCurrentTrafficMode() throws Exception {
        for (TrafficMode trafficMode : trafficModes) {
            if (trafficCode == trafficMode.getTrafficCode()) {
                return trafficMode;
            }
        }
        throw new Exception("사용자의 현재 trafficCode[" + trafficCode + "]와 일치하는 trafficMode가 존재하지 않습니다.");
    }

    public void updatePosition(Position updatePosition) {
        if (position == null) {
            position = updatePosition;
        } else {
            position.updatePosition(updatePosition);
        }
    }
}
