package com.psychologist.site.booking;

import java.util.regex.Pattern;

/** How the client wants to be contacted, with the rule that checks the entered contact. */
public enum ContactMethod {
    PHONE {
        @Override
        public boolean isValid(String contact) {
            return contact.replaceAll("\\D", "").length() >= 9;
        }
    },
    EMAIL {
        private final Pattern email = Pattern.compile(".+@.+\\..+");

        @Override
        public boolean isValid(String contact) {
            return email.matcher(contact).matches();
        }
    },
    INSTAGRAM {
        @Override
        public boolean isValid(String contact) {
            return contact.length() >= 3;
        }
    },
    MESSENGER {
        @Override
        public boolean isValid(String contact) {
            return contact.length() >= 3;
        }
    };

    public abstract boolean isValid(String contact);
}
