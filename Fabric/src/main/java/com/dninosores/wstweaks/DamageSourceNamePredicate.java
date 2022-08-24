package com.dninosores.wstweaks;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.predicate.entity.DamageSourcePredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

class DamageSourceNamePredicate extends DamageSourcePredicate {
    private String name;
    public DamageSourceNamePredicate(String name) {
        super(null, null, null, null, null, null, null, null, null, null);
        this.name = name;
    }

    @Override
    public boolean test(ServerWorld world, Vec3d pos, DamageSource damageSource) {
        return damageSource.name.equals(name);
    }

    public static class NameBuilder extends DamageSourcePredicate.Builder {
        private String setName;

        public Builder setName(String name) {
            this.setName = name;
            return this;
        }

        public DamageSourcePredicate build() {
            return new DamageSourceNamePredicate(setName);
        }
    }
}
