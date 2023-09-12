package dopamine.backend.challenge.service;

import dopamine.backend.challenge.entity.Challenge;
import dopamine.backend.challenge.mapper.ChallengeMapper;
import dopamine.backend.challenge.repository.ChallengeCustomRepository;
import dopamine.backend.challenge.repository.ChallengeRepository;
import dopamine.backend.challenge.request.ChallengeEditDTO;
import dopamine.backend.challenge.request.ChallengeRequestDTO;
import dopamine.backend.challenge.response.ChallengeResponseDTO;
import dopamine.backend.challengemember.entity.ChallengeMember;
import dopamine.backend.member.entity.Member;
import dopamine.backend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final ChallengeCustomRepository challengeCustomRepository;

    private final ChallengeMapper challengeMapper;

    private final MemberService memberService;

    private Challenge verifiedChallenge(Long challengeId) {
        return challengeRepository.findById(challengeId).orElseThrow(() -> new RuntimeException("존재하지 않는 챌린지입니다."));
    }

    public void createChallenge(ChallengeRequestDTO challengeRequestDTO) {

        Challenge challenge = challengeMapper.challengeRequestDtoToChallenge(challengeRequestDTO);

        challengeRepository.save(challenge);
    }

    public void deleteChallenge(Long challengeId) {
        Challenge challenge = verifiedChallenge(challengeId);

        challenge.changeDelYn(true);
    }

    public void deleteChallengeHard(Long challengeId) {
        Challenge challenge = verifiedChallenge(challengeId);

        challengeRepository.delete(challenge);
    }

    @Transactional(readOnly = true)
    public ChallengeResponseDTO getChallenge(Long challengeId) {
        Challenge challenge = verifiedChallenge(challengeId);

        ChallengeResponseDTO challengeResponseDTO = challengeMapper.challengeToChallengeResponseDTO(challenge);

        return challengeResponseDTO;
    }

    public void editChallenge(Long challengeId, ChallengeEditDTO challengeEditDTO) {
        Challenge challenge = verifiedChallenge(challengeId);

        challenge.changeChallenge(challengeEditDTO);
    }

    public List<ChallengeResponseDTO> todayChallenge(Long userId) {

        Member member = memberService.verifiedMember(userId);
        List<ChallengeResponseDTO> challengeResponseList;

        // 기존에 챌린지 받은 적 없음
        LocalDateTime existRefreshDate = member.getChallengeRefreshDate();
        if(existRefreshDate == null){
            List<Challenge> todayChallenges = challengeCustomRepository.getTodayChallenges(null);
            challengeResponseList = getChallengeResponseList(todayChallenges);
        }

        else {
            LocalDateTime today = LocalDateTime.now();

            String todayInfo = getFormatDate(today);
            String existInfo = getFormatDate(existRefreshDate);

            List<Challenge> exitChallenge = member.getChallengeMembers().stream().map(ChallengeMember::getChallenge).collect(Collectors.toList());

            // 갱신
            if(!todayInfo.equals(existInfo)){
                List<Challenge> todayChallenges = challengeCustomRepository.getTodayChallenges(exitChallenge);
                challengeResponseList = getChallengeResponseList(todayChallenges);
            }
            // 조회
            else {
                challengeResponseList = getChallengeResponseList(exitChallenge);
            }
        }

        return challengeResponseList;
    }

    private static String getFormatDate(LocalDateTime today) {
        return today.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"));
    }

    private List<ChallengeResponseDTO> getChallengeResponseList(List<Challenge> challenges) {
        return challenges.stream().map(challengeMapper::challengeToChallengeResponseDTO).collect(Collectors.toList());
    }
}
