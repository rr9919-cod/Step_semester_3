class LibraryMember {
    private String membershipPin;
    String branchCode;
    protected double finesOwed;
    public String displayName;
}

public class AccessChecker {

    static String classifyAccess(String fieldModifier, String accessorContext) {

        if (accessorContext.equals("SAME_CLASS")) {
            return "ALLOWED";
        }

        if (accessorContext.equals("SAME_PACKAGE")) {
            if (fieldModifier.equals("private")) {
                return "DENIED";
            }
            return "ALLOWED";
        }

        if (accessorContext.equals("DIFFERENT_PACKAGE")) {
            if (fieldModifier.equals("public")) {
                return "ALLOWED";
            }
            return "DENIED";
        }

        if (accessorContext.equals("SUBCLASS_DIFFERENT_PACKAGE_OWN_TYPE")) {
            if (fieldModifier.equals("protected")
                    || fieldModifier.equals("public")) {
                return "ALLOWED";
            }
            return "DENIED";
        }

        if (accessorContext.equals("SUBCLASS_DIFFERENT_PACKAGE_PARENT_TYPE")) {
            if (fieldModifier.equals("public")) {
                return "ALLOWED";
            }
            return "DENIED";
        }

        return "DENIED";
    }

    static String summarizeByModifier(String[][] attempts) {

        int privateAllowed = 0;
        int privateDenied = 0;

        int defaultAllowed = 0;
        int defaultDenied = 0;

        int protectedAllowed = 0;
        int protectedDenied = 0;

        int publicAllowed = 0;
        int publicDenied = 0;

        for (String[] attempt : attempts) {

            String modifier = attempt[0];
            String context = attempt[1];

            String result = classifyAccess(modifier, context);

            if (modifier.equals("private")) {
                if (result.equals("ALLOWED")) {
                    privateAllowed++;
                } else {
                    privateDenied++;
                }
            } else if (modifier.equals("default")) {
                if (result.equals("ALLOWED")) {
                    defaultAllowed++;
                } else {
                    defaultDenied++;
                }
            } else if (modifier.equals("protected")) {
                if (result.equals("ALLOWED")) {
                    protectedAllowed++;
                } else {
                    protectedDenied++;
                }
            } else if (modifier.equals("public")) {
                if (result.equals("ALLOWED")) {
                    publicAllowed++;
                } else {
                    publicDenied++;
                }
            }
        }

        return "private: " + privateAllowed + " allowed / "
                + privateDenied + " denied | default: "
                + defaultAllowed + " allowed / "
                + defaultDenied + " denied | protected: "
                + protectedAllowed + " allowed / "
                + protectedDenied + " denied | public: "
                + publicAllowed + " allowed / "
                + publicDenied + " denied";
    }
}
