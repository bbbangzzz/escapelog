package com.recoders.escapelog.service;

import com.recoders.escapelog.domain.Member;
import com.recoders.escapelog.domain.Recode;
import com.recoders.escapelog.domain.Theme;
import com.recoders.escapelog.dto.RecodeDto;
import com.recoders.escapelog.repository.LibraryRepository;
import com.recoders.escapelog.repository.ThemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class LibraryService {

    private final LibraryRepository libraryRepository;
    private final ThemeRepository themeRepository;

    public void saveRecode(Member member, RecodeDto recodeDto){

        Theme theme = null;
        if(recodeDto.getThemeNo() != null){
            theme = themeRepository.findById(recodeDto.getThemeNo()).get();
        }

        Recode recode = Recode.builder()
                .member(member)
                .theme(theme)
                .title(recodeDto.getTitle())
                .contents(recodeDto.getContents())
                .rating(recodeDto.getRating())
                .breakTime(recodeDto.getBreakTime())
                .hint(recodeDto.getHint())
                .success(recodeDto.getSuccess())
                .playerNum(recodeDto.getPlayerNum())
                .regdate(LocalDateTime.now().withNano(0))
                .build();

        libraryRepository.save(recode);
    }

    @Transactional
    public List<Recode> getRecodeList() {

        List<Recode> recodeEntities = libraryRepository.findAll();
        List<Recode> recodeList = new ArrayList<>();

        for(Recode recodes : recodeEntities) {
            Recode recode = Recode.builder()
                    .title(recodes.getTitle())
                    .regdate(recodes.getRegdate())
                    .theme(recodes.getTheme())
                    .build();

            recodeList.add(recode);
        }

        return recodeList;
    }

    @Transactional
    public List<Recode> getMemberRecodeList(String nickname) {
        List<Recode> recodeEntities = libraryRepository.findByMember_NicknameOrderByRegdateDesc(nickname);
        List<Recode> recodeList = new ArrayList<>();

        for(Recode recodes : recodeEntities) {
            Recode recode = Recode.builder()
                    .no(recodes.getNo())
                    .title(recodes.getTitle())
                    .regdate(recodes.getRegdate())
                    .theme(recodes.getTheme())
                    .build();

            recodeList.add(recode);
        }

        return recodeList;
    }

    @Transactional
    public Recode getRecode(Long no) {
        Optional<Recode> optionalRecode = libraryRepository.findById(no);

        if(optionalRecode.isEmpty()){
            throw new IllegalArgumentException("wrong recode no");
        }

        Recode recode = optionalRecode.get();
        recode.setNlString(System.getProperty("line.separator").toString());
        return recode;
    }

    @Transactional
    public void deleteRecode(Long no){
        libraryRepository.deleteByNo(no);
    }

    @Transactional
    public List<Recode> searchRecode(String keyword) {
        List<Recode> recodeEntities = libraryRepository.findByTheme_ThemeNameContaining(keyword);
        List<Recode> searchRecodeList = new ArrayList<>();

        if (recodeEntities.isEmpty()) {
            return searchRecodeList;
        }

        for(Recode recodes : recodeEntities) {
            Recode recode = Recode.builder()
                    .no(recodes.getNo())
                    .title(recodes.getTitle())
                    .regdate(recodes.getRegdate())
                    .theme(recodes.getTheme())
                    .build();

            searchRecodeList.add(recode);
        }

        return searchRecodeList;
    }



}
