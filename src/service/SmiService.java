package service;

import model.SmiData;
import model.SmiRow;

public class SmiService {

    public void compute(SmiData data) {
        int previousTotal = 0;

        for (int i = 0; i < data.getRows().size(); i++) {
            SmiRow row = data.getRows().get(i);

            int total = previousTotal + row.getAddedModules() - row.getDeletedModules();
            row.setTotalModules(total);

            if (total <= 0) {
                row.setSmi(0.0);
            } else {
                double smi = (double) (total - (
                        row.getAddedModules()
                                + row.getChangedModules()
                                + row.getDeletedModules()
                )) / total;

                row.setSmi(smi);
            }

            previousTotal = total;
        }
    }
}