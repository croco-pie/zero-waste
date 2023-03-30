package com.zerowaste.zwb.service;

import com.zerowaste.zwb.dto.WasteDTO;
import com.zerowaste.zwb.entity.WasteEntity;
import com.zerowaste.zwb.enums.WasteTypeEnum;
import com.zerowaste.zwb.repository.WasteRepository;
import com.zerowaste.zwb.service.impl.MarkupCodeProcessingServiceImpl;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureEmbeddedDatabase
@ActiveProfiles(profiles = "unit")
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class MarkupCodeProcessingServiceTest {
    @Autowired
    private WasteRepository wasteRepository;
    private MarkupCodeProcessingService markupCodeProcessingService;

    @BeforeAll
    public void setUp() {
        markupCodeProcessingService = new MarkupCodeProcessingServiceImpl(wasteRepository);
    }

    @ParameterizedTest
    @MethodSource("provideArgsForFindByWasteCodeOrMarkup")
    public void test_findByWasteCodeOrMarkup(String code, String expected) {
        String actual = markupCodeProcessingService.findByWasteCodeOrMarkup(code);

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("provideArgsForFindByType")
    public void test_findCodeByType(WasteTypeEnum type, List<String> expected) {
        List<String> actual = markupCodeProcessingService.findCodeNamesAndNumsAndDescriptionsByType(type);

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("provideArgsForCheckIfCodeNumExists")
    public void test_checkIfCodeNumExists(Integer codeNum, boolean expected) {
        boolean actual = markupCodeProcessingService.checkIfCodeNumExists(codeNum);

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("provideArgsForAddWasteCode")
    public void test_addWasteCode(Integer expectedCodeNum, String expectedCodeName, String expectedDescription,
                                  WasteTypeEnum expectedType) {
        WasteDTO actual = markupCodeProcessingService.addWasteCode(expectedCodeNum, expectedCodeName, expectedDescription);
        WasteEntity found = wasteRepository.findAllByCodeNum(expectedCodeNum).stream()
                .filter(entity -> expectedCodeName.equals(entity.getCodeName()))
                .findFirst()
                .get();

        assertEquals(expectedCodeNum, actual.getCodeNum());
        assertEquals(expectedCodeName, actual.getCodeName());
        assertEquals(expectedDescription, actual.getCodeDescription());
        assertEquals(expectedType, found.getWasteType());
        assertFalse(found.isShown());
        assertTrue(found.isAddedByUsers());
    }

    @Test
    public void test_findExistingWasteTypes() {
        List<WasteTypeEnum> actual = markupCodeProcessingService.findExistingWasteTypes();

        assertEquals(5, actual.size());
    }

    private static Stream<Arguments> provideArgsForFindByWasteCodeOrMarkup() {
        return Stream.of(
                Arguments.of("2", "Полиэтилен высокой плотности: упаковка для бытовой химии, некоторых напитков"),
                Arguments.of("40", "Жестяные банки из-под консервов"),
                Arguments.of("ALU", "Алюминий, банки и тюбики от крема"),
                Arguments.of("GL", "70 - GL - Стекло\n\n71 - GL - Стекло\n\n72 - GL - Стекло\n\n73 - GL - Стекло\n\n74 - GL - Стекло"),
                Arguments.of("TETRAPAK", "81 - TETRAPAK - Многослойная упаковка для соков, молока и т.д.\n\n82 - TETRAPAK - Многослойная упаковка для соков, молока и т.д.\n\n84 - TETRAPAK - Многослойная упаковка для соков, молока и т.д."),
                Arguments.of("NOT_FOUND_CODE", null),
                Arguments.of("", null),
                Arguments.of(null, null)
        );
    }

    private static Stream<Arguments> provideArgsForFindByType() {
        return Stream.of(
                Arguments.of(WasteTypeEnum.METAL, List.of("40 FE", "41 ALU")),
                Arguments.of(WasteTypeEnum.PLASTIC, List.of("1 PET", "2 HDPE", "3 PVC", "6 PS", "7 OTHER")),
                Arguments.of(WasteTypeEnum.OTHER, emptyList())
        );
    }

    private static Stream<Arguments> provideArgsForCheckIfCodeNumExists() {
        return Stream.of(
                Arguments.of(0, true),
                Arguments.of(1, true),
                Arguments.of(123, false)
        );
    }

    private static Stream<Arguments> provideArgsForAddWasteCode() {
        return Stream.of(
                Arguments.of(0, "TEST", "Existing", WasteTypeEnum.OTHER),
                Arguments.of(11, "TEST", "Not existing", WasteTypeEnum.PLASTIC),
                Arguments.of(39, "TEST", "End code", WasteTypeEnum.PAPER),
                Arguments.of(60, "TEST", "Begin code", WasteTypeEnum.TEXTILE)
        );
    }
}
