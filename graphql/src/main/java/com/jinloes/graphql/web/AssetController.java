package com.jinloes.graphql.web;

import graphql.com.google.common.collect.Lists;
import java.util.List;
import java.util.UUID;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class AssetController {
  private final List<Asset> assets;

  public AssetController() {
    this.assets = Lists.newArrayList(new Asset("123", "asset1"), new Asset("456", "asset2"));
  }

  @QueryMapping public List<Asset> getAssets() {
    return assets;
  }

  @MutationMapping public Asset createAsset(@Argument String name) {
    Asset asset = new Asset(UUID.randomUUID().toString(), name);
    assets.add(asset);
    return asset;
  }
}
