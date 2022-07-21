package com.csuse.jpetstoressm.persistence;

import com.csuse.jpetstoressm.domain.Sequence;
import org.springframework.stereotype.Repository;

@Repository
public interface SequenceMapper {

    Sequence getSequence(Sequence sequence);

    void updateSequence(Sequence sequence);
}
