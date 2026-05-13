package org.informatics.winter_olympics.service.impl;

import lombok.AllArgsConstructor;
import org.informatics.winter_olympics.data.entity.Athlete;
import org.informatics.winter_olympics.data.entity.BiathlonCompetition;
import org.informatics.winter_olympics.data.entity.BiathlonResult;
import org.informatics.winter_olympics.data.entity.Competition;
import org.informatics.winter_olympics.data.entity.MedalType;
import org.informatics.winter_olympics.data.entity.Olympics;
import org.informatics.winter_olympics.data.entity.Result;
import org.informatics.winter_olympics.data.entity.SlalomCompetition;
import org.informatics.winter_olympics.data.entity.SlalomResult;
import org.informatics.winter_olympics.data.repository.BiathlonResultRepository;
import org.informatics.winter_olympics.data.repository.CompetitionRepository;
import org.informatics.winter_olympics.data.repository.OlympicsRepository;
import org.informatics.winter_olympics.data.repository.SlalomResultRepository;
import org.informatics.winter_olympics.dto.MedalCountDto;
import org.informatics.winter_olympics.dto.MedalistDto;
import org.informatics.winter_olympics.dto.OlympicsStatisticsDto;
import org.informatics.winter_olympics.dto.RankingEntryDto;
import org.informatics.winter_olympics.dto.SlalomResultDto;
import org.informatics.winter_olympics.exception.CompetitionNotFoundException;
import org.informatics.winter_olympics.exception.OlympicsNotFoundException;
import org.informatics.winter_olympics.service.RankingService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Period;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class RankingServiceImpl implements RankingService {

    private final CompetitionRepository competitionRepository;
    private final OlympicsRepository olympicsRepository;
    private final SlalomResultRepository slalomResultRepository;
    private final BiathlonResultRepository biathlonResultRepository;
    private final SlalomResultServiceImpl slalomResultService;

    @Override
    public List<RankingEntryDto> getCompetitionRanking(long competitionId) {
        Competition competition = competitionRepository.findById(competitionId)
                .orElseThrow(() -> new CompetitionNotFoundException(competitionId));

        if (competition instanceof SlalomCompetition) {
            return getSlalomRanking(competitionId);
        }
        if (competition instanceof BiathlonCompetition) {
            return getBiathlonRanking(competitionId);
        }
        return List.of();
    }

    @Override
    public List<SlalomResultDto> getRunTwoStartOrder(long competitionId) {
        return slalomResultService.getRunTwoStartOrder(competitionId);
    }

    @Override
    public List<MedalCountDto> getMedalTable(long olympicsId) {
        olympicsRepository.findById(olympicsId).orElseThrow(() -> new OlympicsNotFoundException(olympicsId));
        Map<String, MedalCountDto> medalTable = new LinkedHashMap<>();

        for (Competition competition : competitionRepository.findByOlympicsId(olympicsId)) {
            for (RankingEntryDto entry : getCompetitionRanking(competition.getId())) {
                if (entry.getMedal() == null) {
                    continue;
                }
                MedalCountDto countryRow = medalTable.getOrDefault(entry.getCountry(),
                        MedalCountDto.builder().country(entry.getCountry()).build());
                addMedal(countryRow, entry.getMedal());
                medalTable.put(entry.getCountry(), countryRow);
            }
        }

        return medalTable.values()
                .stream()
                .sorted(Comparator.comparing(MedalCountDto::getTotalMedals).reversed())
                .toList();
    }

    @Override
    public OlympicsStatisticsDto getOlympicsStatistics(long olympicsId) {
        Olympics olympics = olympicsRepository.findById(olympicsId)
                .orElseThrow(() -> new OlympicsNotFoundException(olympicsId));
        List<Result> results = new ArrayList<>();
        results.addAll(slalomResultRepository.findByCompetitionOlympicsId(olympicsId));
        results.addAll(biathlonResultRepository.findByCompetitionOlympicsId(olympicsId));

        List<MedalistDto> medalists = getMedalists(olympicsId);

        return OlympicsStatisticsDto.builder()
                .olympicsId(olympics.getId())
                .olympicsName(olympics.getName())
                .averageAge(calculateAverageAge(results))
                .youngestMedalist(findYoungestMedalist(medalists))
                .oldestMedalist(findOldestMedalist(medalists))
                .medalTable(getMedalTable(olympicsId))
                .build();
    }

    private List<RankingEntryDto> getSlalomRanking(long competitionId) {
        List<SlalomResult> rankedResults = slalomResultRepository.findByCompetitionId(competitionId)
                .stream()
                .filter(result -> result.getTotalTime() != null)
                .sorted(Comparator.comparing(SlalomResult::getTotalTime))
                .toList();

        List<RankingEntryDto> ranking = new ArrayList<>();
        for (int i = 0; i < rankedResults.size(); i++) {
            SlalomResult result = rankedResults.get(i);
            ranking.add(mapRankingEntry(result, result.getTotalTime(), i + 1));
        }
        return ranking;
    }

    private List<RankingEntryDto> getBiathlonRanking(long competitionId) {
        List<BiathlonResult> rankedResults = biathlonResultRepository.findByCompetitionId(competitionId)
                .stream()
                .filter(result -> result.getFinalTime() != null)
                .sorted(Comparator.comparing(BiathlonResult::getFinalTime))
                .toList();

        List<RankingEntryDto> ranking = new ArrayList<>();
        for (int i = 0; i < rankedResults.size(); i++) {
            BiathlonResult result = rankedResults.get(i);
            ranking.add(mapRankingEntry(result, result.getFinalTime(), i + 1));
        }
        return ranking;
    }

    private RankingEntryDto mapRankingEntry(Result result, BigDecimal finalTime, int position) {
        return RankingEntryDto.builder()
                .position(position)
                .athleteId(result.getAthlete().getId())
                .athleteName(result.getAthlete().getFirstName() + " " + result.getAthlete().getLastName())
                .country(result.getAthlete().getCountry())
                .competitionId(result.getCompetition().getId())
                .competitionName(result.getCompetition().getName())
                .finalTime(finalTime)
                .medal(getMedal(position))
                .build();
    }

    private MedalType getMedal(int position) {
        if (position == 1) {
            return MedalType.GOLD;
        }
        if (position == 2) {
            return MedalType.SILVER;
        }
        if (position == 3) {
            return MedalType.BRONZE;
        }
        return null;
    }

    private void addMedal(MedalCountDto countryRow, MedalType medal) {
        if (medal == MedalType.GOLD) {
            countryRow.setGoldMedals(countryRow.getGoldMedals() + 1);
        }
        if (medal == MedalType.SILVER) {
            countryRow.setSilverMedals(countryRow.getSilverMedals() + 1);
        }
        if (medal == MedalType.BRONZE) {
            countryRow.setBronzeMedals(countryRow.getBronzeMedals() + 1);
        }
        countryRow.setTotalMedals(countryRow.getTotalMedals() + 1);
    }

    private BigDecimal calculateAverageAge(List<Result> results) {
        if (results.isEmpty()) {
            return BigDecimal.ZERO;
        }
        double averageAge = results.stream()
                .mapToInt(result -> Period.between(result.getAthlete().getDateOfBirth(),
                        result.getCompetition().getCompetitionDate()).getYears())
                .average()
                .orElse(0);
        return BigDecimal.valueOf(averageAge).setScale(1, RoundingMode.HALF_UP);
    }

    private List<MedalistDto> getMedalists(long olympicsId) {
        List<MedalistDto> medalists = new ArrayList<>();
        for (Competition competition : competitionRepository.findByOlympicsId(olympicsId)) {
            for (RankingEntryDto entry : getCompetitionRanking(competition.getId())) {
                if (entry.getMedal() == null) {
                    continue;
                }
                Athlete athlete = findAthlete(entry, competition.getId());
                medalists.add(MedalistDto.builder()
                        .name(entry.getAthleteName())
                        .country(entry.getCountry())
                        .age(Period.between(athlete.getDateOfBirth(), competition.getCompetitionDate()).getYears())
                        .medal(entry.getMedal())
                        .competitionName(entry.getCompetitionName())
                        .build());
            }
        }
        return medalists;
    }

    private Athlete findAthlete(RankingEntryDto entry, long competitionId) {
        return slalomResultRepository.findByCompetitionId(competitionId)
                .stream()
                .filter(result -> result.getAthlete().getId() == entry.getAthleteId())
                .map(SlalomResult::getAthlete)
                .findFirst()
                .orElseGet(() -> biathlonResultRepository.findByCompetitionId(competitionId)
                        .stream()
                        .filter(result -> result.getAthlete().getId() == entry.getAthleteId())
                        .map(BiathlonResult::getAthlete)
                        .findFirst()
                        .orElseThrow());
    }

    private MedalistDto findYoungestMedalist(List<MedalistDto> medalists) {
        return medalists.stream()
                .min(Comparator.comparing(MedalistDto::getAge))
                .orElse(null);
    }

    private MedalistDto findOldestMedalist(List<MedalistDto> medalists) {
        return medalists.stream()
                .max(Comparator.comparing(MedalistDto::getAge))
                .orElse(null);
    }
}
